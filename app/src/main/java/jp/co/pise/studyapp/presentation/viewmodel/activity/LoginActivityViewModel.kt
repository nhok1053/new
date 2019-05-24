package jp.co.pise.studyapp.presentation.viewmodel.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import jp.co.pise.studyapp.definition.Message
import jp.co.pise.studyapp.domain.model.LoginChallenge
import jp.co.pise.studyapp.domain.model.LoginUser
import jp.co.pise.studyapp.domain.usecase.UserLogin
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.default
import jp.co.pise.studyapp.presentation.StudyAppException
import jp.co.pise.studyapp.presentation.viewmodel.BaseViewModel
import javax.inject.Inject

class LoginActivityViewModel @Inject constructor(private val userLogin: UserLogin) : BaseViewModel() {
    val loginId = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = this._message

    private val _isLoading = MutableLiveData<Boolean>().default(false)
    val isLoading: LiveData<Boolean> = this._isLoading

    private val onLoginSubject = PublishSubject.create<LoginUser>()
    val onLogin: Observable<LoginUser> = this.onLoginSubject

    fun login() {
        if (TextUtils.isEmpty(this.loginId.value) || TextUtils.isEmpty(this.password.value)) {
            this._message.postValue(Message.ID_PASSWORD_NOT_ENTERED)
        } else {
            this._isLoading.postValue(true)
            val model = LoginChallenge(this.loginId.value!!, this.password.value!!)
            this.userLogin.login(model).observeOn(AndroidSchedulers.mainThread()).subscribe({
                this.onLoginSubject.onNext(it)
                this._isLoading.postValue(false)
            }, { t ->
                var message = Message.LOGIN_FAILED
                if (t is StudyAppException) {
                    if (!TextUtils.isEmpty(t.displayMessage))
                        message = t.displayMessage
                }
                this._message.postValue(message)
                this._isLoading.postValue(false)
            }).addBug(this.subscriptions)
        }
    }
}