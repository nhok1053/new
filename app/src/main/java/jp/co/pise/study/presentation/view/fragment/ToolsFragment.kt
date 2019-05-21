package jp.co.pise.study.presentation.view.fragment

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import jp.co.pise.study.R
import jp.co.pise.study.databinding.FragmentToolsBinding
import jp.co.pise.study.extension.addBug
import jp.co.pise.study.presentation.view.adapter.ToolListAdapter
import jp.co.pise.study.presentation.viewmodel.fragment.ToolsFragmentViewModel
import javax.inject.Inject

class ToolsFragment : BaseFragment() {
    @Inject
    lateinit var viewModel: ToolsFragmentViewModel

    private lateinit var binding: FragmentToolsBinding
    private lateinit var adapter: ToolListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private var isInitialize = false

    companion object {
        @JvmStatic
        fun newInstance() = ToolsFragment()
    }

    override fun onAttach(context: Context) {
        if (!isInitialize) AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (!isInitialize) {
            this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tools, container, false)

            this.layoutManager = LinearLayoutManager(activity)
            this.adapter = ToolListAdapter(this)
            this.viewModel.tools.observe(this, Observer {
                this.adapter.entities.postValue(it)
            })
            this.viewModel.addBug(this.disableObserver.subscriptions)

            this.binding.viewModel = this.viewModel
            this.binding.recyclerView.layoutManager = this.layoutManager
            this.binding.recyclerView.adapter = this.adapter

            this.viewModel.getFirstData()
        }
        isInitialize = true
        return this.binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
    }
}
