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
import jp.co.pise.studyapp.databinding.FragmentCouponBinding
import jp.co.pise.studyapp.domain.model.GetCouponItemModel
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.extension.replaceObserve
import jp.co.pise.studyapp.extension.unwrap
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.view.adapter.CouponListAdapter
import jp.co.pise.studyapp.presentation.viewmodel.fragment.CouponFragmentViewModel
import javax.inject.Inject

class CouponFragment : BaseFragment() {
    @Inject
    lateinit var viewModel: CouponFragmentViewModel
    private lateinit var binding: FragmentCouponBinding
    private lateinit var adapter: CouponListAdapter
    private var useCouponConfirmListener: UseCouponConfirmListener? = null

    interface UseCouponConfirmListener {
        fun onUsedCouponConfirm(model: GetCouponItemModel)
    }

    companion object {
        const val TAG = "CouponFragment"

        fun newInstance(): CouponFragment {
            return CouponFragment()
        }
    }

    fun useCoupon(model: GetCouponItemModel) {
        this.adapter.useCoupon(model)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        if (context is UseCouponConfirmListener) {
            this.useCouponConfirmListener = context
        } else {
            this.useCouponConfirmListener = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.binding = DataBindingUtil
                .inflate<FragmentCouponBinding>(inflater, R.layout.fragment_coupon, container, false)
                .owner(this)
        this.binding.viewModel = this.viewModel
        this.viewModel.addBug(this.subscriptions)

        // setting swipe refresh
        this.binding.swipeRefresh.setOnRefreshListener { initViewModel() }

        // setting adapter
        this.adapter = CouponListAdapter(this.viewModel.couponList, this)
        this.binding.recyclerView.layoutManager = LinearLayoutManager(context)
        this.binding.recyclerView.adapter = this.adapter
        this.adapter.onItemClick.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ itemViewModel -> useCouponConfirm(itemViewModel.toItemModel()) }, { }).addBug(this.subscriptions)
        this.adapter.addBug(this.subscriptions)

        // setting view model
        this.viewModel.onLoginExpired.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loginExpired) { }.addBug(this.subscriptions)
        this.viewModel.isRefreshing.replaceObserve(this, Observer {
            if (!it.unwrap && binding.swipeRefresh.isRefreshing) {
                binding.swipeRefresh.isRefreshing = false
            }
        })

        // setting app message
        StudyApp.instance.onLoginStateChange
                .subscribe({ initViewModel() }, { }).addBug(this.subscriptions)
        StudyApp.instance.onRefreshedUsedCoupon
                .subscribe({ initViewModel() }, { }).addBug(this.subscriptions)

        initViewModel()

        if (!this.viewModel.isRefreshing.value.unwrap)
            this.binding.swipeRefresh.isRefreshing = false

        return this.binding.root
    }

    private fun initViewModel() {
        if (StudyApp.instance.isLoggedIn && StudyApp.instance.loginUser != null) {
            this.viewModel.initialize(true, StudyApp.instance.loginUser)
        } else {
            this.viewModel.initialize(false, null)
        }
    }

    private fun useCouponConfirm(model: GetCouponItemModel) {
        this.useCouponConfirmListener?.onUsedCouponConfirm(model)
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
