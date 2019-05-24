package jp.co.pise.studyapp.presentation.view.adapter

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.ItemNewsListBinding
import jp.co.pise.studyapp.extension.resizeFromDimen
import jp.co.pise.studyapp.presentation.viewmodel.adapter.NewsListItemViewModel

class NewsListAdapter(viewModels: ObservableArrayList<NewsListItemViewModel>, owner: LifecycleOwner, context: Context) : BaseAdapter<NewsListItemViewModel, NewsListAdapter.ViewHolder>(viewModels, owner, context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_list, parent, false)
        return ViewHolder(view, owner, this.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(this.viewModels[position])
    }

    override fun getItemCount(): Int {
        return this.viewModels.size
    }

    override fun dispose() {
        if (!this.subscriptions.isDisposed)
            this.subscriptions.dispose()
    }

    override fun isDisposed(): Boolean {
        return this.subscriptions.isDisposed
    }

    class ViewHolder(view: View, owner: LifecycleOwner, val context: Context) : RecyclerView.ViewHolder(view) {
        private val binding: ItemNewsListBinding = DataBindingUtil.bind(view)!!

        init {
            this.binding.lifecycleOwner = owner
        }

        fun update(viewModel: NewsListItemViewModel) {
            this.binding.viewModel = viewModel
            updateImage(viewModel)
        }

        private fun updateImage(viewModel: NewsListItemViewModel) {
            try {
                if (!TextUtils.isEmpty(viewModel.imageUrl.value)) {
                    this.binding.image.resizeFromDimen(
                            this.context,
                            viewModel.imageUrl.value,
                            R.dimen.news_image_width,
                            R.dimen.news_image_height)
                    this.binding.image.visibility = View.VISIBLE
                } else {
                    this.binding.image.visibility = View.INVISIBLE
                }
            } catch (e: Exception) {
                this.binding.image.visibility = View.INVISIBLE
            }
        }
    }
}
