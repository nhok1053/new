package jp.co.pise.studyapp.presentation.viewmodel.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import jp.co.pise.studyapp.domain.model.LoginUser
import jp.co.pise.studyapp.domain.usecase.UserLogin
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.default
import jp.co.pise.studyapp.presentation.StudyAppException
import jp.co.pise.studyapp.presentation.viewmodel.BaseViewModel
import javax.inject.Inject

class SplashActivityViewModel @Inject constructor(private val userLogin: UserLogin) : BaseViewModel() {
    init {
        this.userLogin.addBug(this.subscriptions)
    }

    private val _isLoading = MutableLiveData<Boolean>().default(false)
    val isLoading: LiveData<Boolean> = this._isLoading

    private val onLoginSubject = PublishSubject.create<LoginUser>()
    val onLogin: Observable<LoginUser> = this.onLoginSubject

    private val onLoginErrorSubject = PublishSubject.create<StudyAppException>()
    val onLoginError: Observable<StudyAppException> =  this.onLoginErrorSubject

    fun autoLogin() {
        this._isLoading.postValue(true)
        this.userLogin.autoLogin().observeOn(AndroidSchedulers.mainThread()).subscribe(
                { result ->
                    this.onLoginSubject.onNext(result)
                    this._isLoading.postValue(false)
                },
                { t -> autoLoginError(t) { this._isLoading.postValue(false) } }).addBug(this.subscriptions)
    }

    private fun autoLoginError(t: Throwable, action: (() -> Unit)? = null) {
        this.userLogin.logout().observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    this.onLoginErrorSubject.onNext(StudyAppException.fromThrowable(t))
                    action?.invoke()
                }, {
                    this.onLoginErrorSubject.onNext(StudyAppException.fromThrowable(t))
                    action?.invoke()
                }).addBug(this.subscriptions)
    }
}
