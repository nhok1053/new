package jp.co.pise.studyapp.presentation.viewmodel.adapter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import jp.co.pise.studyapp.domain.model.GetNewsItemModel
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.viewmodel.BaseViewModel
import javax.inject.Inject

class NewsListItemViewModel @Inject constructor() : BaseViewModel() {

    // region <----- data ----->

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = this._imageUrl

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = this._description

    private val _url = MutableLiveData<String>()
    val url: LiveData<String> = this._url

    private var _sortOrder: Int = 0
    val sortOrder: Int get() = this._sortOrder

    // endregion

    // <----- factory ----->

    companion object {
        fun fromResultItem(newsItem: GetNewsItemModel): NewsListItemViewModel {
            val viewModel = StudyApp.instance.appComponent.createNewsListItemViewModel()
            viewModel._imageUrl.postValue(newsItem.imageUrl)
            viewModel._description.postValue(newsItem.description)
            viewModel._url.postValue(newsItem.url)
            viewModel._sortOrder = newsItem.sortOrder
            return viewModel
        }
    }

    // endregion
}
