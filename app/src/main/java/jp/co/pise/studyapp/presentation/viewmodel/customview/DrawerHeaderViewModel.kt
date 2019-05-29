package jp.co.pise.studyapp.presentation.viewmodel.customview

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import jp.co.pise.studyapp.domain.model.LoginUser
import jp.co.pise.studyapp.extension.default
import jp.co.pise.studyapp.extension.unwrap
import jp.co.pise.studyapp.presentation.viewmodel.BaseViewModel
import javax.inject.Inject

private const val DEFAULT_HEADER_TEXT = "Guest User"

class DrawerHeaderViewModel @Inject constructor() : BaseViewModel() {

    private val _isLogin = MutableLiveData<Boolean>().default(false)
    val isLogin: LiveData<Boolean> = this._isLogin

    private val _loginUser = MutableLiveData<LoginUser?>()
    val loginUser: LiveData<LoginUser?> = this._loginUser

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = this._imageUrl

    val headerText = MediatorLiveData<String>().apply {
        addSource(_isLogin) { this.value = getHeaderText(it.unwrap, _loginUser.value) }
        addSource(_loginUser) { this.value = getHeaderText(_isLogin.value.unwrap, it) }
    }

    private fun getHeaderText(login: Boolean, user: LoginUser?): String {
        return if (login && user != null) user.displayName.unwrap
        else DEFAULT_HEADER_TEXT
    }

    fun setLoginState(isLogin: Boolean, loginUser: LoginUser?) {
        if (isLogin && loginUser != null) {
            this._isLogin.postValue(true)
            this._loginUser.postValue(loginUser)
        } else {
            this._isLogin.postValue(false)
            this._loginUser.postValue(null)
        }

        if (isLogin && loginUser != null)
            this._imageUrl.postValue(loginUser.imageUrl)
        else
            this._imageUrl.postValue(null)
    }
}
