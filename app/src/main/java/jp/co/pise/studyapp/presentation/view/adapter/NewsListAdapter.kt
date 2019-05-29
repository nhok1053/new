package jp.co.pise.studyapp.presentation.view.adapter

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.ItemNewsListBinding
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.extension.replaceObserve
import jp.co.pise.studyapp.extension.resizeFromDimen
import jp.co.pise.studyapp.presentation.viewmodel.adapter.NewsListItemViewModel

class NewsListAdapter(viewModels: ObservableArrayList<NewsListItemViewModel>, owner: LifecycleOwner) : BaseAdapter<NewsListItemViewModel, NewsListAdapter.ViewHolder>(viewModels, owner) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_list, parent, false)
        return ViewHolder(view, owner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(this.viewModels[position])
    }

    override fun getItemCount(): Int {
        return this.viewModels.size
    }

    class ViewHolder(root: View, private val owner: LifecycleOwner) : RecyclerView.ViewHolder(root) {
        val binding =
                DataBindingUtil.bind<ItemNewsListBinding>(root)!!.owner(this.owner)

        fun update(viewModel: NewsListItemViewModel) {
            this.binding.viewModel = viewModel
            viewModel.imageUrl.replaceObserve(this.owner, Observer { updateImage(it) })
        }

        private fun updateImage(imageUrl: String?) {
            try {
                if (!TextUtils.isEmpty(imageUrl)) {
                    this.binding.image.resizeFromDimen(
                            imageUrl,
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
