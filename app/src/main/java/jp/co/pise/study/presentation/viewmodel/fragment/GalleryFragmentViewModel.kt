package jp.co.pise.study.presentation.viewmodel.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import jp.co.pise.study.data.entity.Gallery
import jp.co.pise.study.extension.addAll
import jp.co.pise.study.extension.default
import jp.co.pise.study.extension.map
import jp.co.pise.study.presentation.viewmodel.BaseViewModel
import java.util.ArrayList
import javax.inject.Inject

class GalleryFragmentViewModel @Inject constructor() : BaseViewModel() {
    private val _galleries = MutableLiveData<MutableList<Gallery>>().default(mutableListOf())
    val galleries: LiveData<List<Gallery>> = _galleries.map { it.toList() }

    fun getGalleries() {
        val list = ArrayList<Gallery>()
        for (index in 0..99) {
            list.add(Gallery(index.toString(), "Gallery $index"))
        }
        _galleries.addAll(list)
    }
}