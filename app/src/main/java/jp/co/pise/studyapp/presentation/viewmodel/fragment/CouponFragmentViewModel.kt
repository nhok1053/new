package jp.co.pise.studyapp.presentation.viewmodel.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import jp.co.pise.studyapp.domain.model.GetCouponChallenge
import jp.co.pise.studyapp.domain.model.LoginUser
import jp.co.pise.studyapp.domain.usecase.CouponDisplay
import jp.co.pise.studyapp.domain.usecase.UserLogin
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.default
import jp.co.pise.studyapp.extension.unwrap
import jp.co.pise.studyapp.presentation.viewmodel.LoginOperationViewModel
import jp.co.pise.studyapp.presentation.viewmodel.adapter.CouponListItemViewModel
import javax.inject.Inject

class CouponFragmentViewModel @Inject constructor(userLogin: UserLogin, private val couponDisplay: CouponDisplay) : LoginOperationViewModel(userLogin) {

    private var getCouponDisposable: Disposable? = null

    // region <----- data ----->

    private val _isLogin = MutableLiveData<Boolean>().default(false)
    val isLogin: LiveData<Boolean> = this._isLogin

    private val _loginUser = MutableLiveData<LoginUser?>()
    val loginUser: LiveData<LoginUser?> = this._loginUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = this._isLoading

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = this._isRefreshing

    val couponList = ObservableArrayList<CouponListItemViewModel>()

    // endregion

    // region <----- function ----->

    fun initialize(isLogin: Boolean, loginUser: LoginUser?) {
        this._isLogin.postValue(isLogin)
        this._loginUser.postValue(loginUser)

        this._isLoading.postValue(true)
        getCoupon { this._isLoading.postValue(false) }
    }

    fun setLoginUser(loginUser: LoginUser?) {
        this._loginUser.postValue(loginUser)
        this.couponList.forEach { it.loginUser.postValue(loginUser) }
    }

    fun refresh() {
        this._isLoading.postValue(true)
        this._isRefreshing.postValue(true)
        getCoupon {
            this._isLoading.postValue(false)
            this._isRefreshing.postValue(false)
        }
    }

    private fun getCoupon(action: (() -> Unit)?) {
        if (this.getCouponDisposable != null) {
            if (!this.getCouponDisposable!!.isDisposed)
                this.getCouponDisposable?.dispose()

            this.getCouponDisposable = null
        }

        this.couponList.clear()
        val model = GetCouponChallenge(this._isLogin.value.unwrap, this._loginUser.value)
        this.getCouponDisposable = this.couponDisplay.getCoupon(model).observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
            result.coupons?.sortedBy {
                it.sortOrder
            }?.map {
                CouponListItemViewModel.fromResultItem(it, _isLogin.value.unwrap, _loginUser.value)
            }?.toList()?.let {
                couponList.addAll(it)
            }
            action?.invoke()

        }, { t -> checkLoginExpired(t, action) })
        this.getCouponDisposable?.addBug(this.subscriptions)
    }

    // endregion
}
