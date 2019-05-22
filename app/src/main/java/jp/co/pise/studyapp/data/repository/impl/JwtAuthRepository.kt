package jp.co.pise.studyapp.data.repository.impl

import android.text.TextUtils
import com.squareup.moshi.Json
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.schedulers.Schedulers
import jp.co.pise.studyapp.data.entity.OrmaDatabase
import jp.co.pise.studyapp.definition.ApiConfig
import jp.co.pise.studyapp.definition.ResultCode
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.onSafeError
import jp.co.pise.studyapp.framework.retrofit.UserApiInterface
import jp.co.pise.studyapp.presentation.StudyAppException

abstract class JwtAuthRepository constructor(val userApi: UserApiInterface, val db: OrmaDatabase) : BaseRepository() {
    /**
     *
     * @param func Apiをコールする関数
     * @param model Apiのコールに使用するModel
     * @param <M> funcの引数の型
     * @param <R> Apiの返却の型
     * @return funcを実施した結果がトークン期限切れの場合、トークンをリフレッシュしてから再度更新するSingle
    </R></M> */
    protected fun <M : JwtAuthChallengeModel, R : ApiResultModel> ((M) -> Single<R>).callWithTokenRefresh(model: M): Single<R> {
        return Single.create { emitter ->
            this(model).subscribe({ result ->
                if (result.apiResultCode === ApiResultCode.TokenExpired) {
                    this.refreshAndRetry(emitter, model)
                } else {
                    emitter.onSuccess(result)
                }
            }, { t ->
                if (t is StudyAppException) {
                    if (t.resultCode === ResultCode.LoginExpired) {
                        this.refreshAndRetry(emitter, model)
                    } else {
                        emitter.onSafeError(t)
                    }
                } else {
                    emitter.onSafeError(t)
                }
            }).addBug(subscriptions)
        }
    }

    /**
     * トークンをリフレッシュしてfuncを再度実施するメソッド
     *
     * @param emitter 結果を通知するSingleEmitter
     * @param func Apiをコールする関数
     * @param model Apiのコールに使用するModel
     * @param <M> funcの引数の型
     * @param <R> Apiの返却の型
    </R></M> */
    private fun <M : JwtAuthChallengeModel, R : ApiResultModel> ((M) -> Single<R>).refreshAndRetry(emitter: SingleEmitter<R>, model: M) {
        doRefreshToken(model).subscribe({ refreshTokenResult ->
            try {
                if (refreshTokenResult.apiResultCode === ApiResultCode.Success) {
                    model.accessToken = refreshTokenResult.accessToken!!
                    this(model).subscribe(emitter::onSuccess, emitter::onSafeError).addBug(subscriptions)
                } else {
                    emitter.onSafeError(refreshTokenResult.apiResultCode)
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }, emitter::onSafeError).addBug(subscriptions)
    }

    /**
     * トークン更新処理
     * ※refreshTokenのAPIをコールし、DBへ保存する
     * @return トークンリフレッシュ結果を通知するObservable
     */
    private fun <M : JwtAuthChallengeModel> doRefreshToken(model: M): Single<RefreshTokenApiResult> {
        return refreshToken().flatMap { refreshTokenResult ->
            Single.create<RefreshTokenApiResult> { emitter ->
                try {
                    if (refreshTokenResult.apiResultCode == ApiResultCode.Success) {
                        val challenge = SaveTokenChallenge(
                                model.id,
                                refreshTokenResult.accessToken!!,
                                refreshTokenResult.refreshToken!!)

                        saveToken(challenge).subscribe({ emitter.onSuccess(refreshTokenResult) }, emitter::onSafeError).addBug(this.subscriptions)
                    } else {
                        emitter.onSafeError(refreshTokenResult.apiResultCode)
                    }
                } catch (e: Exception) {
                    emitter.onSafeError(e)
                }
            }
        }
    }

    /**
     * トークン更新処理
     * @return APIのコール結果
     */
    private fun refreshToken(): Single<RefreshTokenApiResult> {
        return Single.create<RefreshTokenApiResult> { emitter ->
            try {
                val user = db.selectFromUser().getOrNull(0)
                if (user != null && !TextUtils.isEmpty(user.accessToken) && !TextUtils.isEmpty(user.refreshToken)) {

                    val challenge = RefreshTokenApiChallenge(user.accessToken, user.refreshToken)
                    this.userApi.refreshToken(challenge).subscribeOn(Schedulers.io()).subscribe({ response ->
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!)
                        } else {
                            emitter.onSafeError(response)
                        }
                    }, emitter::onSafeError).addBug(this.subscriptions)
                } else {
                    emitter.onSafeError(StudyAppException.fromCode(ResultCode.LoginExpired))
                }
            } catch (e: Exception) {
                emitter.onSafeError(e)
            }
        }
    }

    /**
     * トークン情報をDBに保存
     * @param model 保存する値を格納したModel
     * @return 保存結果
     */
    private fun saveToken(model: SaveTokenChallenge): Single<SaveTokenResult> {
        return Single.create<SaveTokenResult> { emitter ->
            try {
                val user = db.selectFromUser().idEq(model.id).getOrNull(0)
                if (user != null) {
                    db.updateUser().
                            idEq(user.id)
                            .accessToken(model.accessToken)
                            .refreshToken(model.refreshToken)
                            .execute()

                    emitter.onSuccess(SaveTokenResult())
                } else {
                    emitter.onSafeError(ResultCode.UserNotLogin)
                }
            } catch (e: Exception) {
                emitter.onSafeError(e)
            }
        }.subscribeOn(Schedulers.io())
    }
}

// region <----- models ----->

abstract class JwtAuthChallengeModel(
        @Transient val id: String,
        @Transient var accessToken: String) {

    val authorizationValue: String?
        get() = ApiConfig.authorizationValue(this.accessToken)
}

class RefreshTokenApiChallenge(@Json(name = "accessToken") val accessToken: String,
                               @Json(name = "refreshToken") val refreshToken: String)

class RefreshTokenApiResult : ApiResultModel() {

    @Json(name = "accessToken")
    var accessToken: String? = null
        private set

    @Json(name = "refreshToken")
     var refreshToken: String? = null
        private set
}

class SaveTokenChallenge(val id: String,
                         val accessToken: String,
                         val refreshToken: String)

class SaveTokenResult

// endregion