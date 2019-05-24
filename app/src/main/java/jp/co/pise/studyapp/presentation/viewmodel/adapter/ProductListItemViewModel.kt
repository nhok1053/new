package jp.co.pise.studyapp.presentation.viewmodel.adapter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import jp.co.pise.studyapp.definition.Utility
import jp.co.pise.studyapp.domain.model.ProductItemModel
import jp.co.pise.studyapp.extension.map
import jp.co.pise.studyapp.extension.unwrap
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.viewmodel.BaseViewModel
import javax.inject.Inject

private const val PRICE_WITHOUT_TAX_FORMAT = "%s"
private const val PRICE_IN_TAX_FORMAT = "(税込：%s)"

class ProductListItemViewModel @Inject constructor() : BaseViewModel() {

    // region <----- data ----->

    private lateinit var _id: String
    val id get() = this._id

    private val _priceWithoutTax = MutableLiveData<Int>()
    val priceWithoutTax: LiveData<String> = this._priceWithoutTax.map { Utility.formatJpCurrency(it.toLong(), PRICE_WITHOUT_TAX_FORMAT) }

    private val _priceInTax = MutableLiveData<Int>()
    val priceInTax: LiveData<String> = this._priceInTax.map { Utility.formatJpCurrency(it.toLong(), PRICE_IN_TAX_FORMAT) }

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = this._name

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = this._description

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = this._imageUrl

    private val onShowDetailSubject = PublishSubject.create<ProductListItemViewModel>()
    val onShowDetail: Observable<ProductListItemViewModel> = this.onShowDetailSubject

    // endregion

    // region <----- factory ----->

    companion object {
        fun fromResultItem(model: ProductItemModel): ProductListItemViewModel {
            val viewModel = StudyApp.instance.appComponent.createProductListItemViewModel()

            viewModel._id = model.id
            viewModel._name.postValue(model.name)
            viewModel._imageUrl.postValue(model.imageUrl)
            viewModel._description.postValue(model.description)
            viewModel._priceWithoutTax.postValue(model.priceWithoutTax)
            viewModel._priceInTax.postValue(model.priceInTax)
            return viewModel
        }
    }

    // endregion

    // region <----- function ----->

    fun toItemModel(): ProductItemModel {
        return ProductItemModel(
                this._id,
                this._name.value,
                this._description.value,
                this._imageUrl.value,
                this._priceWithoutTax.value.unwrap,
                this._priceInTax.value.unwrap
        )
    }

    fun showDetail() {
        this.onShowDetailSubject.onNext(this)
    }

    // endregion
}
