package jp.co.pise.studyapp.presentation.viewmodel

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.definition.ResultCode
import jp.co.pise.studyapp.domain.usecase.UserLogin
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.framework.rx.LoginExpiredMessage
import jp.co.pise.studyapp.presentation.StudyAppException

abstract class LoginOperationViewModel(protected var userLogin: UserLogin) : BaseViewModel() {
    init {
        this.userLogin.addBug(this.subscriptions)
    }

    val onLoginExpired: Observable<LoginExpiredMessage> = this.messenger.register(LoginExpiredMessage::class.java)

    protected fun doLoginExpired(throwable: Throwable, action: (() -> Unit)? = null) = doLoginExpired(throwable, action, true)
    protected fun doLoginExpired(throwable: Throwable, action: (() -> Unit)? = null, isDoRequiredAction: Boolean): Boolean {
        var isDoLoginExpired = false
        try {
            if (throwable is StudyAppException) {
                when (throwable.resultCode) {
                    ResultCode.UserNotLogin, ResultCode.LoginExpired -> {
                        isDoLoginExpired = true
                        this.userLogin.logout()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        {
                                            action?.invoke()
                                            sendLoginExpiredMessage()
                                        },
                                        {
                                            action?.invoke()
                                            sendLoginExpiredMessage()
                                        }).addBug(this.subscriptions)
                    }

                    else -> if (isDoRequiredAction) action?.invoke()
                }
            }
        } catch (e: Exception) {
            isDoLoginExpired = false
        }

        return isDoLoginExpired
    }

    protected fun sendLoginExpiredMessage() {
        this.messenger.send(LoginExpiredMessage())
    }
}
