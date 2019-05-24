package jp.co.pise.studyapp.presentation.viewmodel.adapter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import jp.co.pise.studyapp.definition.Constant
import jp.co.pise.studyapp.definition.Utility
import jp.co.pise.studyapp.domain.model.GetUsedCouponItemModel
import jp.co.pise.studyapp.extension.map
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.viewmodel.BaseViewModel
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class UsedCouponListItemViewModel @Inject constructor() : BaseViewModel() {

    // region <----- data ----->

    private val _priceWithoutTax = MutableLiveData<Int>()
    val priceWithoutTax: LiveData<String> = this._priceWithoutTax.map { NumberFormat.getInstance().format(it.toLong()) }

    private val _priceInTax = MutableLiveData<Int>()
    val priceInTax: LiveData<String> = this._priceInTax.map { NumberFormat.getInstance().format(it.toLong()) }

    private val _usedTime = MutableLiveData<Date>()
    val usedTime: LiveData<String> = this._usedTime.map { Utility.dateParseString(it, Constant.DEFAULT_FORMAT_YMDHM); }

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = this._name

    // endregion

    // region <----- factory ----->

    companion object {
        fun fromResultItem(model: GetUsedCouponItemModel): UsedCouponListItemViewModel {
            val viewModel = StudyApp.instance.appComponent.createUsedCouponListItemViewModel()
            viewModel._name.postValue(model.name)
            viewModel._priceWithoutTax.postValue(model.priceWithoutTax)
            viewModel._priceInTax.postValue(model.priceInTax)
            viewModel._usedTime.postValue(model.usedTime)
            return viewModel
        }
    }

    // endregion
}
