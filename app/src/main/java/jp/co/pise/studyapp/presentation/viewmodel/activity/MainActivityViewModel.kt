package jp.co.pise.studyapp.presentation.viewmodel.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import jp.co.pise.studyapp.domain.model.LoginUser
import jp.co.pise.studyapp.domain.model.RefreshUsedCouponChallenge
import jp.co.pise.studyapp.domain.usecase.CouponUse
import jp.co.pise.studyapp.domain.usecase.UserLogin
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.default
import jp.co.pise.studyapp.extension.unwrap
import jp.co.pise.studyapp.presentation.viewmodel.LoginOperationViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(userLogin: UserLogin, private val couponUse: CouponUse) : LoginOperationViewModel(userLogin) {
    init {
        this.couponUse.addBug(this.subscriptions)
    }

    private val onLogoutSubject = PublishSubject.create<Unit>()
    val onLogout: Observable<Unit> = this.onLogoutSubject

    private val onRefreshUsedCouponSubject = PublishSubject.create<Boolean>()
    val onRefreshUsedCoupon: Observable<Boolean> = this.onRefreshUsedCouponSubject

    private val _isLoading = MutableLiveData<Boolean>().default(false)
    val isLoading: LiveData<Boolean> = this._isLoading

    fun logout() {
        if (!this.isLoading.value.unwrap) {
            this._isLoading.postValue(true)
            this.userLogin.logout().observeOn(AndroidSchedulers.mainThread()).subscribe({
                this.onLogoutSubject.onNext(Unit)
                this._isLoading.postValue(false)
            }, {
                this.onLogoutSubject.onNext(Unit)
                this._isLoading.postValue(false)
            }).addBug(this.subscriptions)
        }
    }

    fun refreshUsedCoupon(loginUser: LoginUser) {
        if (!this.isLoading.value.unwrap) {
            this._isLoading.postValue(true)
            val model = RefreshUsedCouponChallenge(loginUser)
            this.couponUse.refreshUsedCoupon(model).observeOn(AndroidSchedulers.mainThread()).subscribe({
                this.onRefreshUsedCouponSubject.onNext(true)
                this._isLoading.postValue(false)
            }, { t ->
                val isDoLoginExpired = doLoginExpired(t) {
                    this.onRefreshUsedCouponSubject.onNext(false)
                    this._isLoading.postValue(false)
                }
                if (!isDoLoginExpired) {
                    this.onRefreshUsedCouponSubject.onNext(false)
                    this._isLoading.postValue(false)
                }
            }).addBug(this.subscriptions)
        }
    }
}
