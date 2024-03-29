package jp.co.pise.studyapp.presentation.view.fragment.child

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.FragmentProductBinding
import jp.co.pise.studyapp.domain.model.ProductItemModel
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.extension.replaceObserve
import jp.co.pise.studyapp.extension.unwrap
import jp.co.pise.studyapp.presentation.view.adapter.ProductListAdapter
import jp.co.pise.studyapp.presentation.view.fragment.BaseFragment
import jp.co.pise.studyapp.presentation.view.fragment.dialog.ProductDetailFragment
import jp.co.pise.studyapp.presentation.viewmodel.fragment.child.ProductFragmentViewModel
import javax.inject.Inject

private const val SPAN_COUNT = 2

class ProductFragment : BaseFragment() {
    @Inject
    lateinit var viewModel: ProductFragmentViewModel
    private lateinit var binding: FragmentProductBinding
    private lateinit var adapter: ProductListAdapter

    companion object {
        const val TAG = "ProductFragment"

        fun newInstance(): ProductFragment {
            return ProductFragment()
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

        // setting binding
        this.binding = DataBindingUtil
                .inflate<FragmentProductBinding>(inflater, R.layout.fragment_product, container, false)
                .owner(this)
        this.binding.viewModel = this.viewModel
        this.viewModel.addBug(this.subscriptions)

        // setting swipe refresh
        this.binding.swipeRefresh.setOnRefreshListener { this.viewModel.refresh() }

        // setting adapter
        this.adapter = ProductListAdapter(this.viewModel.productList, this)
        this.binding.recyclerView.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        this.binding.recyclerView.adapter = this.adapter
        this.adapter.onItemClick.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ itemViewModel -> showProductDetail(itemViewModel.toItemModel()) }, { }).addBug(this.subscriptions)
        this.adapter.addBug(this.subscriptions)

        // setting view model message
        this.viewModel.onLoginExpired.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::doLoginExpired) { }.addBug(this.subscriptions)
        this.viewModel.isRefreshing.replaceObserve(this, Observer {
            if (!it.unwrap && binding.swipeRefresh.isRefreshing) {
                binding.swipeRefresh.isRefreshing = false
            }
        })

        this.viewModel.initialize()

        return this.binding.root
    }

    private fun showProductDetail(model: ProductItemModel) {
        // Fragmentの表示をシングルトンにする為、既に表示されていた場合は閉じる
        (fragmentManager?.findFragmentByTag(ProductDetailFragment.TAG) as ProductDetailFragment?)?.dismiss()

        val fragment = ProductDetailFragment.newInstance(model)
        fragment.show(fragmentManager, ProductDetailFragment.TAG)
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
