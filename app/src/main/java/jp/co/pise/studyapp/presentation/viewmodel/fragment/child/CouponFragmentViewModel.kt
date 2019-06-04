package jp.co.pise.studyapp.presentation.viewmodel.fragment.child

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
import jp.co.pise.studyapp.presentation.viewmodel.LoginOperationViewModel
import jp.co.pise.studyapp.presentation.viewmodel.adapter.CouponListItemViewModel
import javax.inject.Inject

class CouponFragmentViewModel @Inject constructor(userLogin: UserLogin, private val couponDisplay: CouponDisplay) : LoginOperationViewModel(userLogin) {
    init {
        this.couponDisplay.addBug(this.subscriptions)
    }

    private var getCouponDisposable: Disposable? = null

    // region <----- data ----->

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = this._isLoading

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> = this._isRefreshing

    val couponList = ObservableArrayList<CouponListItemViewModel>()

    val name = MutableLiveData<String>()

    // endregion

    // region <----- function ----->

    fun initialize(isLogin: Boolean, loginUser: LoginUser?) {
        this._isLoading.postValue(true)
        this._isRefreshing.postValue(true)
        getCoupon(isLogin, loginUser) {
            this._isLoading.postValue(false)
            this._isRefreshing.postValue(false)
        }
    }

    private fun getCoupon(isLogin: Boolean, loginUser: LoginUser?, action: (() -> Unit)?) {
        this.getCouponDisposable?.dispose()
        this.getCouponDisposable = null

        this.couponList.clear()
        val model = GetCouponChallenge(isLogin, loginUser)
        this.getCouponDisposable = this.couponDisplay.getCoupon(model).observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
            result.coupons?.sortedBy {
                it.sortOrder
            }?.map {
                CouponListItemViewModel.fromResultItem(it, isLogin, loginUser)
            }?.toList()?.let {
                couponList.addAll(it)
            }
            action?.invoke()

        }, { t ->
            if (!doLoginExpired(t, action))
                action?.invoke()
        })
        this.getCouponDisposable?.addBug(this.subscriptions)
    }

    // endregion
}
