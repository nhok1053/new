package jp.co.pise.studyapp.domain.model

import android.text.TextUtils
import io.reactivex.annotations.NonNull
import jp.co.pise.studyapp.data.entity.User

class LoginChallenge(val loginId: String,
                     val password: String) : ProcessChallengeModel()

class LoginResult(val accessToken: String,
                  val refreshToken: String) : ProcessResultModel()

class GetUserChallenge(@NonNull loginUser: LoginUser) : LoggedInProcessChallengeModel(loginUser)

class GetUserResult(val displayName: String?,
                    val imageUrl: String?) : ProcessResultModel()

class SaveUserChallenge(val userId: String,
                        val accessToken: String,
                        val refreshToken: String) {

    fun toEntity(): User? {
        var user: User? = null
        if (!TextUtils.isEmpty(this.userId) && !TextUtils.isEmpty(this.accessToken) && !TextUtils.isEmpty(this.refreshToken)) {
            user = User(this.userId, this.accessToken, this.refreshToken)
        }
        return user
    }
}

class SaveUserResult : ProcessResultModel()

class GetStoredUserChallenge(@NonNull loginUser: LoginUser) : LoggedInProcessChallengeModel(loginUser)

class GetStoredUserResult constructor(val loginId: String,
                                      val accessToken: String,
                                      val refreshToken: String) : ProcessResultModel() {
    fun toEntity(): User {
        return User(this.loginId,
                this.accessToken,
                this.refreshToken)
    }

    companion object {
        fun fromEntity(user: User): GetStoredUserResult {
            return GetStoredUserResult(user.id, user.accessToken, user.refreshToken)
        }
    }
}

class LoginUser(val loginId: String,
                val displayName: String?,
                val imageUrl: String?) : ProcessResultModel()

class LogoutChallenge : ProcessChallengeModel()

class LogoutResult : ProcessResultModel()