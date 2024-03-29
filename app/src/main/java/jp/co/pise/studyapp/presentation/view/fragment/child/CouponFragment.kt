package jp.co.pise.studyapp.presentation.view.fragment.child

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.FragmentCouponBinding
import jp.co.pise.studyapp.definition.Message
import jp.co.pise.studyapp.domain.model.GetCouponItemModel
import jp.co.pise.studyapp.domain.model.LoginUser
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.extension.replaceObserve
import jp.co.pise.studyapp.extension.unwrap
import jp.co.pise.studyapp.framework.rx.LoginStateChangeMessage
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.StudyAppException
import jp.co.pise.studyapp.presentation.view.adapter.CouponListAdapter
import jp.co.pise.studyapp.presentation.view.fragment.BaseFragment
import jp.co.pise.studyapp.presentation.view.fragment.dialog.CouponUseFragment
import jp.co.pise.studyapp.presentation.viewmodel.fragment.child.CouponFragmentViewModel
import javax.inject.Inject

const val USE_COUPON_REQUEST_CODE = 0

class CouponFragment : BaseFragment(), CouponUseFragment.UseCouponListener {
    @Inject
    lateinit var viewModel: CouponFragmentViewModel
    private lateinit var binding: FragmentCouponBinding
    private lateinit var adapter: CouponListAdapter

    companion object {
        const val TAG = "CouponFragment"

        fun newInstance(): CouponFragment {
            return CouponFragment()
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
                .subscribe({ itemViewModel -> doUseCouponConfirm(itemViewModel.toItemModel()) }, { }).addBug(this.subscriptions)
        this.adapter.onUseCouponError.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::doUseCouponError) {}.addBug(this.subscriptions)
        this.adapter.addBug(this.subscriptions)

        // setting view model message
        this.viewModel.onLoginExpired.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::doLoginExpired) { }.addBug(this.subscriptions)
        this.viewModel.isRefreshing.replaceObserve(this, Observer {
            if (!it.unwrap && binding.swipeRefresh.isRefreshing) {
                binding.swipeRefresh.isRefreshing = false
            }
        })

        // setting app message
        StudyApp.instance.onLoginStateChange
                .subscribe(this::onLoginStateChanged) { }.addBug(this.subscriptions)
        StudyApp.instance.onRefreshedUsedCoupon
                .subscribe({ initViewModel() }, { }).addBug(this.subscriptions)

        initViewModel()

        return this.binding.root
    }

    private fun onLoginStateChanged(message: LoginStateChangeMessage) {
        initViewModel(message.isLogin, message.loginUser)
    }

    override fun onUseCoupon(model: GetCouponItemModel) {
        this.adapter.useCoupon(model)
    }

    private fun initViewModel() {
        initViewModel(StudyApp.instance.isLoggedIn, StudyApp.instance.loginUser)
    }

    private fun initViewModel(isLogin: Boolean, loginUser: LoginUser?) {
        if (isLogin && loginUser != null) {
            this.viewModel.initialize(true, loginUser)
        } else {
            this.viewModel.initialize(false, null)
        }
    }

    private fun doUseCouponConfirm(model: GetCouponItemModel) {
        // Fragmentの表示をシングルトンにする為、既に表示されていた場合は閉じる
        (fragmentManager?.findFragmentByTag(CouponUseFragment.TAG) as CouponUseFragment?)?.dismiss()

        val fragment = CouponUseFragment.newInstance(model)
        fragment.setTargetFragment(this, USE_COUPON_REQUEST_CODE)
        fragment.show(fragmentManager, CouponUseFragment.TAG)
    }

    private fun doUseCouponError(ex: StudyAppException) {
        Toast.makeText(context, Message.USE_COUPON_ERROR, Toast.LENGTH_SHORT).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
