package jp.co.pise.studyapp.domain.usecase

import io.reactivex.Single
import jp.co.pise.studyapp.data.entity.User
import jp.co.pise.studyapp.data.repository.GetUserParam
import jp.co.pise.studyapp.data.repository.IUserRepository
import jp.co.pise.studyapp.domain.model.*
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.onSafeError
import javax.inject.Inject

class UserLogin @Inject constructor(private val userRepository: IUserRepository) : Usecase() {

    fun login(model: LoginChallenge): Single<LoginUser> {
        return this.userRepository.login(model).flatMap<LoginResult> { loginResult ->
            Single.create<LoginResult> { emitter ->
                try {
                    val saveUserChallenge = SaveUserChallenge(
                            model.loginId,
                            loginResult.accessToken,
                            loginResult.refreshToken)

                    this.userRepository.saveUser(saveUserChallenge)
                            .subscribe({ emitter.onSuccess(loginResult) }, emitter::onSafeError).addBug(this.subscriptions)
                } catch (e: Exception) {
                    emitter.onSafeError(e)
                }
            }
        }.flatMap<LoginUser> { loginResult ->
            Single.create<LoginUser> { emitter ->
                try {
                    val user = User(model.loginId, loginResult.accessToken, loginResult.refreshToken)
                    this.userRepository.getUser(GetUserParam(user)).subscribe({ getUserResult ->
                        val loginUser = LoginUser(
                                model.loginId,
                                getUserResult.displayName,
                                getUserResult.imageUrl)

                        emitter.onSuccess(loginUser)
                    }, emitter::onSafeError).addBug(this.subscriptions)
                } catch (e: Exception) {
                    emitter.onSafeError(e)
                }
            }
        }
    }

    fun autoLogin(): Single<LoginUser> {
        return this.userRepository.getStoredUser()
                .flatMap<LoginUser> { storedUser ->
                    Single.create<LoginUser> { emitter ->
                        try {
                            val user = User(storedUser.loginId, storedUser.accessToken, storedUser.refreshToken)
                            this.userRepository.getUser(GetUserParam(user)).subscribe({ getUserResult ->
                                val loginUser = LoginUser(
                                        storedUser.loginId,
                                        getUserResult.displayName,
                                        getUserResult.imageUrl)

                                emitter.onSuccess(loginUser)
                            }, emitter::onSafeError).addBug(this.subscriptions)
                        } catch (e: Exception) {
                            emitter.onSafeError(e)
                        }
                    }
                }
    }

    fun logout(): Single<LogoutResult> {
        return this.userRepository.logout(LogoutChallenge())
    }
}
