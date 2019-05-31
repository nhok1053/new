package jp.co.pise.studyapp.presentation.viewmodel.adapter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import jp.co.pise.studyapp.domain.model.GetNewsItemModel
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.viewmodel.BaseViewModel
import javax.inject.Inject

class NewsListItemViewModel @Inject constructor() : BaseViewModel() {

    // region <----- data ----->

    private lateinit var _id: String
    val id get() = this._id

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = this._imageUrl

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = this._description

    private val _url = MutableLiveData<String>()
    val url: LiveData<String> = this._url

    private var _sortOrder: Int = 0
    val sortOrder: Int get() = this._sortOrder

    private val onShowDetailSubject = PublishSubject.create<NewsListItemViewModel>()
    val onShowDetail: Observable<NewsListItemViewModel> = this.onShowDetailSubject

    // endregion

    // <----- factory ----->

    companion object {
        fun fromResultItem(model: GetNewsItemModel): NewsListItemViewModel {
            val viewModel =
                    StudyApp.instance.appComponent.createNewsListItemViewModel()

            viewModel._id = model.id
            viewModel._imageUrl.postValue(model.imageUrl)
            viewModel._description.postValue(model.description)
            viewModel._url.postValue(model.url)
            viewModel._sortOrder = model.sortOrder
            return viewModel
        }
    }

    // endregion

    // region <----- action ----->

    fun toItemModel(): GetNewsItemModel {
        return GetNewsItemModel(
                this._id,
                this._imageUrl.value,
                this._description.value,
                this._url.value,
                this._sortOrder)
    }

    fun showDetail() = this.onShowDetailSubject.onNext(this)

    // endregion
}
