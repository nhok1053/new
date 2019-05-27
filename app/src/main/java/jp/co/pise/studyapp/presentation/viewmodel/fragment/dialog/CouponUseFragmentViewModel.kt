package jp.co.pise.studyapp.presentation.viewmodel.fragment.dialog

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import jp.co.pise.studyapp.definition.Utility
import jp.co.pise.studyapp.domain.model.GetCouponItemModel
import jp.co.pise.studyapp.extension.map
import jp.co.pise.studyapp.extension.unwrap
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.viewmodel.BaseViewModel
import java.util.*
import javax.inject.Inject

private const val PRICE_WITHOUT_TAX_FORMAT = "%s"
private const val PRICE_IN_TAX_FORMAT = "(税込：%s)"

class CouponUseFragmentViewModel @Inject constructor() : BaseViewModel() {

    // region <----- data ----->

    private lateinit var _id: String
    val id get() = this._id

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = this._name

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = this._description

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = this._imageUrl

    private val _priceWithoutTax = MutableLiveData<Int>()
    val priceWithoutTax: LiveData<String> =
            this._priceWithoutTax.map { Utility.formatJpCurrency(it.toLong(), PRICE_WITHOUT_TAX_FORMAT) }

    private val _priceInTax = MutableLiveData<Int>()
    val priceInTax: LiveData<String> =
            this._priceInTax.map { Utility.formatJpCurrency(it.toLong(), PRICE_IN_TAX_FORMAT); }

    private val _productPriceWithoutTax = MutableLiveData<Int>()
    private val _productPriceInTax = MutableLiveData<Int>()
    private val _startDate = MutableLiveData<Date?>()
    private val _endDate = MutableLiveData<Date?>()
    private val _usedLimit = MutableLiveData<Int>()
    private val _sortOrder = MutableLiveData<Int>()
    private val _usedCount = MutableLiveData<Int?>()

    private val onCloseSubject = PublishSubject.create<Unit>()
    val onClose: Observable<Unit> = this.onCloseSubject

    // endregion

    // region <----- factory ----->

    companion object {
        fun fromResultItem(model: GetCouponItemModel): CouponUseFragmentViewModel {
            val viewModel =
                    StudyApp.instance.appComponent.createCouponUseFragmentViewModel()

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
            viewModel._sortOrder.postValue(model.sortOrder)
            return viewModel
        }
    }

    // endregion

    // region <----- function ----->

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
                this._sortOrder.value.unwrap,
                this._usedCount.value)
    }

    fun close() {
        this.onCloseSubject.onNext(Unit)
    }

    // endregion
}
