package jp.co.pise.studyapp.data.repository.impl

import com.squareup.moshi.Json
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import jp.co.pise.studyapp.data.entity.OrmaDatabase
import jp.co.pise.studyapp.data.repository.GetUserParam
import jp.co.pise.studyapp.data.repository.IUserRepository
import jp.co.pise.studyapp.definition.ResultCode
import jp.co.pise.studyapp.domain.model.*
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.onSafeError
import jp.co.pise.studyapp.framework.retrofit.UserApiInterface
import jp.co.pise.studyapp.presentation.StudyAppException
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(userApi: UserApiInterface, db: OrmaDatabase) : JwtAuthRepository(userApi, db), IUserRepository {

    override fun login(model: LoginChallenge): Single<LoginResult> =
            this.userApi.login(LoginApiChallenge.fromModel(model)).subscribeOn(Schedulers.io()).flatMap { response ->
                Single.create<LoginResult> { emitter ->
                    try {
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!.convert())
                        } else {
                            emitter.onSafeError(response)
                        }
                    } catch (e: Exception) {
                        emitter.onSafeError(e)
                    }
                }
            }

    override fun logout(model: LogoutChallenge): Single<LogoutResult> =
            Single.create<LogoutResult> { emitter ->
                try {
                    this.db.deleteFromUser().execute()
                    emitter.onSuccess(LogoutResult())
                } catch (e: Exception) {
                    emitter.onSafeError(e)
                }
            }.subscribeOn(Schedulers.io())

    override fun getUser(model: GetUserParam): Single<GetUserResult> =
            this.doGetUser.callWithTokenRefresh(GetUserApiChallenge.fromModel(model)).flatMap { response ->
                Single.create<GetUserResult> { emitter ->
                    try {
                        if (response.validate()) {
                            emitter.onSuccess(response.body()!!.convert())
                        } else {
                            emitter.onSafeError(response)
                        }
                    } catch (e: Exception) {
                        emitter.onSafeError(e)
                    }
                }
            }

    override fun saveUser(model: SaveUserChallenge): Single<SaveUserResult> =
            Single.create<SaveUserResult> { emitter ->
                try {
                    this.db.transactionSync {
                        this.db.deleteFromUser().execute()
                        val user = model.toEntity()
                        if (user != null) {
                            this.db.insertIntoUser(user)
                            emitter.onSuccess(SaveUserResult())
                        } else {
                            emitter.onSafeError(StudyAppException.fromCode(ResultCode.InternalError))
                        }
                    }
                } catch (e: Exception) {
                    emitter.onSafeError(e)
                }
            }.subscribeOn(Schedulers.io())

    override fun getStoredUser(): Single<GetStoredUserResult> =
            Single.create<GetStoredUserResult> { emitter ->
                try {
                    val user = db.selectFromUser().getOrNull(0)
                    if (user != null) {
                        emitter.onSuccess(GetStoredUserResult.fromEntity(user))
                    } else {
                        emitter.onSafeError(ResultCode.UserNotLogin)
                    }
                } catch (e: Exception) {
                    emitter.onSafeError(e)
                }
            }.subscribeOn(Schedulers.io())

    override fun getStoredUser(model: GetStoredUserChallenge): Single<GetStoredUserResult> =
            Single.create<GetStoredUserResult> { emitter ->
                try {
                    val user = db.selectFromUser().idEq(model.loginUser.loginId).getOrNull(0)
                    if (user != null) {
                        emitter.onSuccess(GetStoredUserResult.fromEntity(user))
                    } else {
                        emitter.onSafeError(ResultCode.UserNotLogin)
                    }
                } catch (e: Exception) {
                    emitter.onSafeError(e)
                }
            }.subscribeOn(Schedulers.io())

    // region <----- process ----->

    private val doGetUser: (GetUserApiChallenge) -> Single<Response<GetUserApiResult>> = {
        this.userApi.getUser(it.authorizationValue, it).subscribeOn(Schedulers.io())
    }

    // endregion
}

// region <----- models ----->

class LoginApiChallenge() {
    constructor(loginId: String,
                password:String) : this() {

        this.loginId = loginId
        this.password = password
    }

    @Json(name = "loginId")
    lateinit var loginId: String
        private set

    @Json(name = "password")
    lateinit var password: String
        private set

    companion object {
        fun fromModel(challenge: LoginChallenge): LoginApiChallenge {
            val model = LoginApiChallenge()
            model.loginId = challenge.loginId
            model.password = challenge.password
            return model
        }
    }
}

class LoginApiResult : ApiResultModel() {

    @Json(name = "accessToken")
    var accessToken: String? = null
        private set

    @Json(name = "refreshToken")
    var refreshToken: String? = null
        private set

    fun convert(): LoginResult {
        return LoginResult(
                this.accessToken!!,
                this.refreshToken!!)
    }
}

class GetUserApiChallenge(@Json(name = "loginId") var loginId: String,
                          accessToken: String) : JwtAuthChallengeModel(loginId, accessToken) {
    companion object {
        fun fromModel(challenge: GetUserParam)
                = GetUserApiChallenge(challenge.user.id, challenge.user.accessToken)
    }
}

class GetUserApiResult : ApiResultModel() {

    @Json(name = "displayName")
    var displayName: String? = null
        private set

    @Json(name = "imageUrl")
    var imageUrl: String? = null
        private set

    fun convert(): GetUserResult {
        return GetUserResult(
                this.displayName,
                this.imageUrl)
    }
}

// endregion
