package jp.co.pise.study.presentation.viewmodel.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import jp.co.pise.study.data.entity.Tool
import jp.co.pise.study.extension.addAll
import jp.co.pise.study.extension.default
import jp.co.pise.study.extension.map
import jp.co.pise.study.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val FIRST_LOAD_DATA_COUNT = 100
private const val ADD_PAGE_ITEM_COUNT = 20
private const val MAX_PAGE_INDEX = 5

class ToolsFragmentViewModel @Inject constructor() : BaseViewModel() {
    private val _tools = MutableLiveData<MutableList<Tool>>().default(mutableListOf())
    private var pageIndex = 0

    val tools: LiveData<List<Tool>> = _tools.map { it.toList() }

    fun getFirstData() {
        this._tools.addAll(createToolList(0, FIRST_LOAD_DATA_COUNT))
    }

    fun loadNextPage() = GlobalScope.launch {
        if (pageIndex <= MAX_PAGE_INDEX) {
            async {
                delay(2000)
                val startIndex = FIRST_LOAD_DATA_COUNT + pageIndex * ADD_PAGE_ITEM_COUNT
                val endIndex = startIndex + ADD_PAGE_ITEM_COUNT
                createToolList(startIndex, endIndex)
            }.await().let {
                _tools.addAll(it)
                pageIndex++
            }
        }
    }

//    fun loadNextPage() {
//        if (pageIndex <= MAX_PAGE_INDEX) {
//            val startIndex = FIRST_LOAD_DATA_COUNT + pageIndex * ADD_PAGE_ITEM_COUNT
//            val endIndex = startIndex + ADD_PAGE_ITEM_COUNT
//            _tools.addAll(createToolList(startIndex, endIndex))
//            pageIndex++
//        }
//    }

    private fun createToolList(from: Int, to: Int): List<Tool>
            = (from until to).map { Tool(it.toString(), "Tool $it") }.toList()
}