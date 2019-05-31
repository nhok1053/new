package jp.co.pise.studyapp.presentation.view.fragment.child

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.FragmentNewsDetailBinding
import jp.co.pise.studyapp.domain.model.GetNewsItemModel
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.extension.replaceObserve
import jp.co.pise.studyapp.extension.resizeFromDimen
import jp.co.pise.studyapp.presentation.view.fragment.BaseFragment
import jp.co.pise.studyapp.presentation.viewmodel.fragment.child.NewsDetailFragmentViewModel

private const val NEWS = "news"

class NewsDetailFragment : BaseFragment() {
    private var viewModel: NewsDetailFragmentViewModel? = null
    private lateinit var binding: FragmentNewsDetailBinding

    companion object {
        const val TAG = "NewsDetailFragment"

        fun newInstance(model: GetNewsItemModel): NewsDetailFragment {
            val fragment = NewsDetailFragment()
            val args = Bundle()
            args.putSerializable(NEWS, model)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.viewModel?.dispose()
        this.viewModel = (arguments?.getSerializable(NEWS) as GetNewsItemModel?)
                ?.let { model -> NewsDetailFragmentViewModel.fromResultItem(model) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.binding = DataBindingUtil
                .inflate<FragmentNewsDetailBinding>(inflater, R.layout.fragment_news_detail, container, false)
                .owner(this)

        this.viewModel?.let {
            it.imageUrl.replaceObserve(this, Observer<String>(this::setImage))
            this.binding.viewModel = it
            it.addBug(this.subscriptions)
        }

        return this.binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    private fun setImage(imageUrl: String?) {
        try {
            if (!TextUtils.isEmpty(imageUrl)) {
                this.binding.image.resizeFromDimen(
                        imageUrl,
                        R.dimen.news_detail_image_width,
                        R.dimen.news_detail_image_height)
                this.binding.image.visibility = View.VISIBLE
            } else {
                this.binding.image.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {
            this.binding.image.visibility = View.INVISIBLE
        }

    }
}
