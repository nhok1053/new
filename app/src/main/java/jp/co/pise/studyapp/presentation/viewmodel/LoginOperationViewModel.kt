package jp.co.pise.studyapp.presentation.viewmodel

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.definition.ResultCode
import jp.co.pise.studyapp.domain.usecase.UserLogin
import jp.co.pise.studyapp.framework.rx.LoginExpiredMessage
import jp.co.pise.studyapp.framework.rx.UserNotLoggedInMessage
import jp.co.pise.studyapp.presentation.StudyAppException

abstract class LoginOperationViewModel(protected var userLogin: UserLogin) : BaseViewModel() {
    val onLoginExpired: Observable<LoginExpiredMessage> = this.messenger.register(LoginExpiredMessage::class.java)
    val onUserNotLoggedIn: Observable<UserNotLoggedInMessage> = this.messenger.register(UserNotLoggedInMessage::class.java)

    protected fun checkLoginExpired(throwable: Throwable, action: (() -> Unit)? = null) {
        try {
            if (throwable is StudyAppException) {
                when (throwable.resultCode) {
                    ResultCode.UserNotLogin, ResultCode.LoginExpired -> this.subscriptions.add(this.userLogin.logout()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        action?.invoke()
                                        sendLoginExpiredMessage()
                                    },
                                    {
                                        action?.invoke()
                                        sendLoginExpiredMessage()
                                    }))

                    else -> action?.invoke()
                }
            }
        } catch (e: Exception) {
        }
    }

    protected fun sendLoginExpiredMessage() {
        this.messenger.send(LoginExpiredMessage())
    }

    protected fun sendUserNotLoggedInMessage() {
        this.messenger.send(UserNotLoggedInMessage())
    }
}
