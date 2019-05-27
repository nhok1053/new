package jp.co.pise.studyapp.presentation.view.fragment

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.FragmentNewsBinding
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.extension.replaceObserve
import jp.co.pise.studyapp.extension.unwrap
import jp.co.pise.studyapp.presentation.view.adapter.NewsListAdapter
import jp.co.pise.studyapp.presentation.viewmodel.fragment.NewsFragmentViewModel
import javax.inject.Inject

class NewsFragment : BaseFragment() {
    @Inject
    lateinit var viewModel: NewsFragmentViewModel
    private lateinit var binding: FragmentNewsBinding
    private lateinit var adapter: NewsListAdapter

    companion object {
        const val TAG = "NewsFragment"

        fun newInstance(): NewsFragment {
            return NewsFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.binding = DataBindingUtil
                .inflate<FragmentNewsBinding>(inflater, R.layout.fragment_news, container, false)
                .owner(this)
        this.binding.viewModel = this.viewModel
        this.viewModel.addBug(this.subscriptions)

        this.binding.swipeRefresh.setOnRefreshListener { this.viewModel.refresh() }

        this.adapter = NewsListAdapter(this.viewModel.newsList, this)
        this.binding.recyclerView.layoutManager = LinearLayoutManager(context)
        this.binding.recyclerView.adapter = this.adapter
        this.adapter.addBug(this.subscriptions)

        this.viewModel.onLoginExpired.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loginExpired) {}.addBug(this.subscriptions)
        this.viewModel.isRefreshing.replaceObserve(this, Observer {
            if (!it.unwrap && binding.swipeRefresh.isRefreshing) {
                binding.swipeRefresh.isRefreshing = false
            }
        })

        this.viewModel.initialize()

        return this.binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
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
}
