package jp.co.pise.study.presentation.view.adapter

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.co.pise.study.R
import jp.co.pise.study.data.entity.Tool
import jp.co.pise.study.databinding.ItemToolListBinding
import jp.co.pise.study.extension.default
import jp.co.pise.study.extension.map
import jp.co.pise.study.presentation.viewmodel.adapter.ToolListItemViewModel

class ToolListAdapter constructor(@NonNull val owner: LifecycleOwner) : RecyclerView.Adapter<ToolListAdapter.ViewHolder>() {
    val entities = MutableLiveData<List<Tool>>().default(mutableListOf())
    private val viewModels = this.entities.map {
        if (it.isEmpty()) {
            mutableListOf()
        } else {
            it.map { entity -> ToolListItemViewModel(entity.getName()) }.toList()
        }
    }

    init {
        this.viewModels.observe(this.owner, Observer {
            this.notifyDataSetChanged()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tool_list, parent, false)
        return ToolListAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(this.viewModels.value!![position])
    }

    override fun getItemCount() = this.viewModels.value?.count() ?: 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: ItemToolListBinding = DataBindingUtil.bind(view)!!

        fun update(viewModel: ToolListItemViewModel) {
            this.binding.viewModel = viewModel
        }
    }
}