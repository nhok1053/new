package jp.co.pise.studyapp.presentation.viewmodel.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import jp.co.pise.studyapp.domain.usecase.NewsDisplay
import jp.co.pise.studyapp.domain.usecase.UserLogin
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.default
import jp.co.pise.studyapp.presentation.viewmodel.LoginOperationViewModel
import jp.co.pise.studyapp.presentation.viewmodel.adapter.NewsListItemViewModel
import javax.inject.Inject

class NewsFragmentViewModel @Inject constructor(userLogin: UserLogin, private val newsDisplay: NewsDisplay) : LoginOperationViewModel(userLogin) {

    private var getNewsDisposable: Disposable? = null

    // region <----- data ----->

    private val _isLoading = MutableLiveData<Boolean>().default(false)
    val isLoading: LiveData<Boolean> = this._isLoading

    private val _isRefreshing = MutableLiveData<Boolean>().default(false)
    val isRefreshing: LiveData<Boolean> = this._isRefreshing

    val newsList = ObservableArrayList<NewsListItemViewModel>()

    // endregion

    // region <----- function ----->

    fun initialize() {
        this._isLoading.postValue(true)
        getNewsList { this._isLoading.postValue(false) }
    }

    fun refresh() {
        this._isLoading.postValue(true)
        this._isRefreshing.postValue(true)
        getNewsList {
            this._isLoading.postValue(false)
            this._isRefreshing.postValue(false)
        }
    }

    private fun getNewsList(action: (() -> Unit)?) {
        if (this.getNewsDisposable != null) {
            if (!this.getNewsDisposable!!.isDisposed)
                this.getNewsDisposable?.dispose()

            this.getNewsDisposable = null
        }

        this.newsList.clear()
        this.getNewsDisposable = this.newsDisplay.getNews().observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
            result.news?.sortedBy {
                it.sortOrder
            }?.map {
                NewsListItemViewModel.fromResultItem(it)
            }?.toList()?.let {
                newsList.addAll(it)
            }
            action?.invoke()
        }, { t -> checkLoginExpired(t, action) })
        this.getNewsDisposable?.addBug(this.subscriptions)
    }

    // endregion
}