package jp.co.pise.study.presentation.view.fragment


import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import jp.co.pise.study.R
import jp.co.pise.study.databinding.FragmentGalleryBinding
import jp.co.pise.study.extension.addBug
import jp.co.pise.study.presentation.view.adapter.GalleryListAdapter
import jp.co.pise.study.presentation.viewmodel.fragment.GalleryFragmentViewModel
import javax.inject.Inject

class GalleryFragment : BaseFragment() {
    @Inject
    lateinit var viewModel: GalleryFragmentViewModel
    private lateinit var binding: FragmentGalleryBinding
    private lateinit var adapter: GalleryListAdapter

    companion object {
        @JvmStatic
        fun newInstance() = GalleryFragment()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)

        this.adapter = GalleryListAdapter(this)
        this.viewModel.galleries.observe(this, Observer { adapter.entities.postValue(it) })
        this.viewModel.addBug(this.disableObserver.subscriptions)

        this.binding.viewModel = viewModel
        this.binding.recyclerView.layoutManager = GridLayoutManager(activity, 4)
        this.binding.recyclerView.adapter = adapter

        this.viewModel.getGalleries()
        return this.binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
