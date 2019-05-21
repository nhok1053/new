package jp.co.pise.study.presentation.viewmodel.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import jp.co.pise.study.data.repository.IUserRepository
import jp.co.pise.study.domain.model.LoginChallenge
import jp.co.pise.study.extension.map
import jp.co.pise.study.extension.switchMap
import jp.co.pise.study.presentation.viewmodel.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val userRepository: IUserRepository) : BaseViewModel() {
    private val _loginChallenge = MutableLiveData<LoginChallenge>()

    val userId = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val loginChallenge: LiveData<LoginChallenge> = _loginChallenge
    val loginResult = _loginChallenge.switchMap { this.userRepository.login(it) }
    val result = loginResult.map { it.result }
    val message = loginResult.map { it.message }
    val token = loginResult.map { it.token }

    val skipCommand: Subject<Boolean> = PublishSubject.create()

    fun login() = this._loginChallenge.postValue(LoginChallenge(this.userId.value, this.password.value))
    fun skip() = this.skipCommand.onNext(true)
}