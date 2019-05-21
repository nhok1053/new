package jp.co.pise.study.presentation.viewmodel.adapter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class ToolListItemViewModel @Inject constructor() : ViewModel() {
    private val _name = MutableLiveData<String?>()
    val name: LiveData<String?> = _name

    constructor(defName: String?): this() { _name.postValue(defName) }
}