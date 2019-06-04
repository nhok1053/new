package jp.co.pise.studyapp.presentation.viewmodel.fragment.child

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import jp.co.pise.studyapp.domain.usecase.ProductDisplay
import jp.co.pise.studyapp.domain.usecase.UserLogin
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.default
import jp.co.pise.studyapp.presentation.viewmodel.LoginOperationViewModel
import jp.co.pise.studyapp.presentation.viewmodel.adapter.ProductListItemViewModel
import javax.inject.Inject

class ProductFragmentViewModel @Inject constructor(userLogin: UserLogin, private val productDisplay: ProductDisplay) : LoginOperationViewModel(userLogin) {
    init {
        this.productDisplay.addBug(this.subscriptions)
    }

    private var getProductDisposable: Disposable? = null

    private val _isLoading = MutableLiveData<Boolean>().default(false)
    val isLoading: LiveData<Boolean> = this._isLoading

    private val _isRefreshing = MutableLiveData<Boolean>().default(false)
    val isRefreshing: LiveData<Boolean> = this._isRefreshing

    val productList = ObservableArrayList<ProductListItemViewModel>()

    fun initialize() {
        this._isLoading.postValue(true)
        getProduct { this._isLoading.postValue(false) }
    }

    fun refresh() {
        this._isLoading.postValue(true)
        this._isRefreshing.postValue(true)
        getProduct {
            this._isLoading.postValue(false)
            this._isRefreshing.postValue(false)
        }
    }

    fun getProduct(action: (() -> Unit)?) {
        this.getProductDisposable?.dispose()
        this.getProductDisposable = null

        this.productList.clear()
        this.getProductDisposable = this.productDisplay.getProduct().observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
            result.products?.map {
                ProductListItemViewModel.fromResultItem(it)
            }?.toList()?.let {
                productList.addAll(it)
            }
            action?.invoke()
        }, { t ->
            if(!doLoginExpired(t, action))
                action?.invoke()
        })
        this.getProductDisposable?.addBug(this.subscriptions)
    }
}
