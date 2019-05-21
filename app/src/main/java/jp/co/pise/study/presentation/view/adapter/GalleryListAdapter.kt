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
import jp.co.pise.study.data.entity.Gallery
import jp.co.pise.study.databinding.ItemGalleryListBinding
import jp.co.pise.study.extension.default
import jp.co.pise.study.extension.map
import jp.co.pise.study.presentation.viewmodel.adapter.GalleryListItemViewModel

class GalleryListAdapter constructor(@NonNull val owner: LifecycleOwner) : RecyclerView.Adapter<GalleryListAdapter.ViewHolder>() {
    val entities = MutableLiveData<List<Gallery>>().default(mutableListOf())
    private val viewModels = this.entities.map {
        if (it.isEmpty()) {
            mutableListOf()
        } else {
            it.map { entity -> GalleryListItemViewModel(entity.getName()) }.toList()
        }
    }

    init {
        this.viewModels.observe(this.owner, Observer {
            this.notifyDataSetChanged()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gallery_list, parent, false)
        return GalleryListAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GalleryListAdapter.ViewHolder, position: Int) {
        holder.update(this.viewModels.value!![position])
    }

    override fun getItemCount() = this.viewModels.value?.count() ?: 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: ItemGalleryListBinding = DataBindingUtil.bind(view)!!

        fun update(viewModel: GalleryListItemViewModel) {
            this.binding.viewModel = viewModel
        }
    }
}