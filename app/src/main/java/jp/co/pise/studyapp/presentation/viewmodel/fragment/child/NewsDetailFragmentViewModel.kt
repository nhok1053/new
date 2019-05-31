package jp.co.pise.studyapp.presentation.viewmodel.fragment.child

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import jp.co.pise.studyapp.domain.model.GetNewsItemModel
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.viewmodel.BaseViewModel
import javax.inject.Inject

class NewsDetailFragmentViewModel @Inject constructor() : BaseViewModel() {
    private lateinit var _id: String
    private var _sortOrder: Int = 0

    val id get() = this._id
    val sortOrder get() = this._sortOrder

    private val _url = MutableLiveData<String>()
    val url: LiveData<String> = this._url

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String> = this._imageUrl

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = this._description

    companion object {

        fun fromResultItem(model: GetNewsItemModel): NewsDetailFragmentViewModel {
            val viewModel =
                    StudyApp.instance.appComponent.createNewsDetailFragmentViewModel()

            viewModel._id = model.id
            viewModel._url.postValue(model.url)
            viewModel._imageUrl.postValue(model.imageUrl)
            viewModel._description.postValue(model.description)
            viewModel._sortOrder = model.sortOrder
            return viewModel
        }
    }
}