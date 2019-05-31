package jp.co.pise.studyapp.presentation.viewmodel.adapter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import jp.co.pise.studyapp.definition.Constant
import jp.co.pise.studyapp.definition.Utility
import jp.co.pise.studyapp.domain.model.GetCouponItemModel
import jp.co.pise.studyapp.domain.model.LoginUser
import jp.co.pise.studyapp.domain.model.UseCouponChallenge
import jp.co.pise.studyapp.domain.usecase.CouponUse
import jp.co.pise.studyapp.domain.usecase.UserLogin
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.default
import jp.co.pise.studyapp.extension.map
import jp.co.pise.studyapp.extension.unwrap
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.StudyAppException
import jp.co.pise.studyapp.presentation.viewmodel.LoginOperationViewModel
import java.util.*
import javax.inject.Inject

private const val USED_LIMIT_DEFAULT_TEXT = "何回でも使える"
private const val USED_COUNT_DEFAULT_TEXT = "未使用"
private const val END_DATE_DEFAULT_TEXT = "利用期間無制限"

private const val USE_COUPON_NO_LOGIN_TITLE = "ログインしてください"
private const val USE_COUPON_LOGIN_TITLE = "クーポンを使う"
private const val USE_COUPON_NO_LOGIN_CAPTION = ""
private const val USE_COUPON_LOGIN_LIMITLESS_CAPTION = ""
private const val USE_COUPON_LOGIN_CAPTION = "(あと%d回)"

private const val USED_LIMIT_FORMAT = "%d回使える"
private const val USED_COUNT_FORMAT = "%d回使用済"

private const val PRICE_WITHOUT_TAX_FORMAT = "%s"
private const val PRICE_IN_TAX_FORMAT = "(税込：%s)"

class CouponListItemViewModel @Inject constructor(userLogin: UserLogin, private val couponUse: CouponUse) : LoginOperationViewModel(userLogin) {
    init {
        this.couponUse.addBug(this.subscriptions)
    }

    // region <----- data ----->

    private lateinit var _id: String
    val id: String get() = this._id

    private var _sortOrder: Int = 0
    val sortOrder: Int get() = this._sortOrder

    private val _priceWithoutTax = MutableLiveData<Int>().default(0)
    val priceWithoutTax: LiveData<String> = this._priceWithoutTax.map { Utility.formatJpCurrency(it.toLong(), PRICE_WITHOUT_TAX_FORMAT) }

    private val _priceInTax = MutableLiveData<Int>().default(0)
    val priceInTax: LiveData<String> = this._priceInTax.map { Utility.formatJpCurrency(it.toLong(), PRICE_IN_TAX_FORMAT) }

    private val _productPriceWithoutTax = MutableLiveData<Int>().default(0)
    val productPriceWithoutTax: LiveData<String> = this._productPriceWithoutTax.map { Utility.formatJpCurrency(it.toLong(), PRICE_WITHOUT_TAX_FORMAT) }

    private val _productPriceInTax = MutableLiveData<Int>().default(0)
    val productPriceInTax: LiveData<String> = this._productPriceInTax.map { Utility.formatJpCurrency(it.toLong(), PRICE_IN_TAX_FORMAT) }

    private val _startDate = MutableLiveData<Date?>().default(null)
    val startDate = this._startDate.map {
        if (it != null) Utility.dateParseString(it, Constant.DEFAULT_FORMAT_YMDHM) else END_DATE_DEFAULT_TEXT }

    private val _endDate = MutableLiveData<Date?>().default(null)
    val endDate = this._endDate.map {
        if (it != null) Utility.dateParseString(it, Constant.DEFAULT_FORMAT_YMDHM) else END_DATE_DEFAULT_TEXT }

    private val _usedLimit = MutableLiveData<Int>().default(0)
    val usedLimit: LiveData<String> = this._usedLimit.map {
        if (it > 0) String.format(USED_LIMIT_FORMAT, it)
        else USED_LIMIT_DEFAULT_TEXT
    }

    private val _usedCount = MutableLiveData<Int?>().default(null)
    val usedCount: LiveData<Int?> = this._usedCount

    private val _isLogin = MutableLiveData<Boolean>().default(false)
    val isLogin: LiveData<Boolean> = this._isLogin

    private val _isLoading = MutableLiveData<Boolean>().default(false)
    val isLoading: LiveData<Boolean> = this._isLoading

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = this._name

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = this._imageUrl

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = this._description

    val loginUser = MutableLiveData<LoginUser>()

    val useCouponTitle: LiveData<String> = this.isLogin.map {
        if (it) USE_COUPON_LOGIN_TITLE
        else USE_COUPON_NO_LOGIN_TITLE
    }

    val useCouponCaption = MediatorLiveData<String>().apply {
        addSource(_isLogin) { this.value = getUseCouponCaption(it.unwrap, _usedLimit.value.unwrap, _usedCount.value) }
        addSource(_usedLimit) { this.value = getUseCouponCaption(_isLogin.value.unwrap, it.unwrap, _usedCount.value) }
        addSource(_usedCount) { this.value = getUseCouponCaption(_isLogin.value.unwrap, _usedLimit.value.unwrap, it) }
    }

    val isCouponUse = MediatorLiveData<Boolean>().apply {
        addSource(_isLogin) { this.value = getIsCouponUse(it.unwrap, _usedLimit.value.unwrap, _usedCount.value) }
        addSource(_usedLimit) { this.value = getIsCouponUse(_isLogin.value.unwrap, it.unwrap, _usedCount.value) }
        addSource(_usedCount) { this.value = getIsCouponUse(_isLogin.value.unwrap, _usedLimit.value.unwrap, it) }
    }

    val isUseCouponLimit = MediatorLiveData<Boolean>().apply {
        addSource(_isLogin) { this.value = getIsUseCouponLimit(it.unwrap, _usedLimit.value.unwrap, _usedCount.value) }
        addSource(_usedLimit) { this.value = getIsUseCouponLimit(_isLogin.value.unwrap, it.unwrap, _usedCount.value) }
        addSource(_usedCount) { this.value = getIsUseCouponLimit(_isLogin.value.unwrap, _usedLimit.value.unwrap, it) }
    }

    private val onUseCouponConfirmSubject: Subject<CouponListItemViewModel> = PublishSubject.create()
    val onUseCouponConfirm: Observable<CouponListItemViewModel> = this.onUseCouponConfirmSubject

    private val onUseCouponErrorSubject: Subject<StudyAppException> = PublishSubject.create()
    val onUseCouponError: Observable<StudyAppException> = this.onUseCouponErrorSubject

    // endregion

    // region <----- factory ----->

    companion object {
        fun fromResultItem(model: GetCouponItemModel, isLogin: Boolean, loginUser: LoginUser?): CouponListItemViewModel {
            val viewModel = StudyApp.instance.appComponent.createCouponListItemViewModel()
            viewModel._id = model.id
            viewModel._name.postValue(model.name)
            viewModel._imageUrl.postValue(model.imageUrl)
            viewModel._description.postValue(model.description)
            viewModel._priceWithoutTax.postValue(model.priceWithoutTax)
            viewModel._priceInTax.postValue(model.priceInTax)
            viewModel._productPriceWithoutTax.postValue(model.productPriceWithoutTax)
            viewModel._productPriceInTax.postValue(model.productPriceInTax)
            viewModel._startDate.postValue(model.startDate)
            viewModel._endDate.postValue(model.endDate)
            viewModel._usedLimit.postValue(model.usedLimit)
            viewModel._usedCount.postValue(model.usedCount)
            viewModel._sortOrder = model.sortOrder
            viewModel._isLogin.postValue(isLogin)
            viewModel.loginUser.postValue(loginUser)
            return viewModel
        }
    }

    // endregion

    // region <----- function ----->

    private fun getUseCouponCaption(login: Boolean, limit: Int, count: Int?): String {
        return if (login)
            if (limit > 0) String.format(USE_COUPON_LOGIN_CAPTION, limit - count.unwrap)
            else USE_COUPON_LOGIN_LIMITLESS_CAPTION
        else USE_COUPON_NO_LOGIN_CAPTION
    }

    private fun getIsCouponUse(login: Boolean, limit: Int, count: Int?): Boolean {
        return if (login)
            if (limit > 0) limit > count.unwrap
            else true
        else false
    }

    private fun getIsUseCouponLimit(login: Boolean, limit: Int, count: Int?): Boolean {
        return if (login && limit > 0) limit <= count.unwrap
        else false
    }

    fun toItemModel(): GetCouponItemModel {
        return GetCouponItemModel(
                this._id,
                this._name.value,
                this._imageUrl.value,
                this._description.value,
                this._priceWithoutTax.value.unwrap,
                this._priceInTax.value.unwrap,
                this._productPriceWithoutTax.value.unwrap,
                this._productPriceInTax.value.unwrap,
                this._startDate.value,
                this._endDate.value,
                this._usedLimit.value.unwrap,
                this._sortOrder,
                this._usedCount.value)
    }

    // endregion

    // region <----- action ----->

    fun useCouponConfirm() {
        this.onUseCouponConfirmSubject.onNext(this)
    }

    fun useCoupon() {
        if (this.isLogin.value.unwrap && !this.isLoading.value.unwrap && this.loginUser.value != null) {
            this._isLoading.postValue(true)
            val model = UseCouponChallenge(this._id, this.loginUser.value!!)
            this.couponUse.useCoupon(model).observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
                this._usedCount.postValue(result.usedCount)
                this._isLoading.postValue(false)
            }, {
                if (!doLoginExpired(it) { this._isLoading.postValue(false) }) {
                    val ex = if (it is StudyAppException) it else StudyAppException.fromThrowable(it)
                    this.onUseCouponErrorSubject.onNext(ex)
                    this._isLoading.postValue(false)
                }
            }).addBug(this.subscriptions)
        }
    }

    // endregion
}
