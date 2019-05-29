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
import retrofit2.Response
import java.net.HttpURLConnection

abstract class JwtAuthRepository constructor(val userApi: UserApiInterface, val db: OrmaDatabase) : BaseRepository() {
    /**
     *
     * @param model Apiのコールに使用するModel
     * @param <M> funcの引数の型
     * @param <R> Apiの返却の型
     * @return funcを実施した結果がトークン期限切れの場合、トークンをリフレッシュしてから再度更新するSingle
    </R></M> */
    protected fun <M : JwtAuthChallengeModel, R : ApiResultModel> ((M) -> Single<Response<R>>).callWithTokenRefresh(model: M): Single<Response<R>> {
        return Single.create { emitter ->
            this(model).subscribe({
                val body = it.body()
                if (it.code() == HttpURLConnection.HTTP_UNAUTHORIZED
                        || body?.apiResultCode == ApiResultCode.TokenExpired) {

                    emitter.refreshAndRetry(this, model)
                } else {
                    emitter.onSuccess(it)
                }
            }, { t ->
                if (t is StudyAppException) {
                    if (t.resultCode === ResultCode.LoginExpired) {
                        emitter.refreshAndRetry(this, model)
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
     * @param func Apiをコールする関数
     * @param model Apiのコールに使用するModel
     * @param <M> funcの引数の型
     * @param <R> Apiの返却の型
    </R></M> */
    private fun <M : JwtAuthChallengeModel, R : ApiResultModel> SingleEmitter<Response<R>>.refreshAndRetry(func: (M) -> Single<Response<R>>, model: M) {
        refreshToken(model).subscribe({ refreshTokenResult ->
            try {
                if (refreshTokenResult.apiResultCode === ApiResultCode.Success) {
                    model.accessToken = refreshTokenResult.accessToken!!
                    func(model).subscribe(this::onSuccess, this::onSafeError).addBug(subscriptions)
                } else {
                    this.onSafeError(refreshTokenResult.apiResultCode)
                }
            } catch (e: Exception) {
                this.onSafeError(e)
            }
        }, this::onSafeError).addBug(subscriptions)
    }

    /**
     * トークン更新処理
     * ※refreshTokenのAPIをコールし、DBへ保存する
     * @return APIのコール結果
     */
    private fun <M : JwtAuthChallengeModel> refreshToken(model: M): Single<RefreshTokenApiResult> =
            Single.create<Response<RefreshTokenApiResult>> { emitter ->
                try {
                    val user = db.selectFromUser().getOrNull(0)
                    if (user != null && !TextUtils.isEmpty(user.accessToken) && !TextUtils.isEmpty(user.refreshToken)) {
                        val challenge = RefreshTokenApiChallenge(user.accessToken, user.refreshToken)
                        this.userApi.refreshToken(challenge).subscribe(emitter::onSuccess, emitter::onSafeError).addBug(this.subscriptions)
                    } else {
                        emitter.onSafeError(StudyAppException.fromCode(ResultCode.LoginExpired))
                    }
                } catch (e: Exception) {
                    emitter.onSafeError(e)
                }
            }.flatMap { response ->
                Single.create<RefreshTokenApiResult> { emitter ->
                    try {
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!)
                        } else {
                            emitter.onSafeError(response)
                        }
                    } catch (e: Exception) {
                        emitter.onSafeError(e)
                    }
                }
            }.flatMap { refreshTokenResult ->
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
            }.subscribeOn(Schedulers.io())

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

    val authorizationValue
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