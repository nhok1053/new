package jp.co.pise.studyapp.presentation.view.fragment.tab

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.FragmentCouponTabBinding
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.presentation.view.fragment.BaseTabFragment
import jp.co.pise.studyapp.presentation.view.fragment.child.CouponFragment
import jp.co.pise.studyapp.presentation.viewmodel.fragment.tab.CouponTabFragmentViewModel
import javax.inject.Inject

class CouponTabFragment : BaseTabFragment() {
    @Inject
    lateinit var viewModel: CouponTabFragmentViewModel
    private lateinit var binding: FragmentCouponTabBinding

    companion object {
        const val TAG = "CouponTabFragment"

        fun newInstance(): CouponTabFragment {
            return CouponTabFragment()
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
                .inflate<FragmentCouponTabBinding>(inflater, R.layout.fragment_coupon_tab, container, false)
                .owner(this)
        this.binding.viewModel = this.viewModel
        this.viewModel.addBug(this.subscriptions)

        // setting fragment
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.container, createCouponFragment(), CouponFragment.TAG)
        transaction.commitNow()

        return this.binding.root
    }

    private fun createCouponFragment(): CouponFragment {
        return CouponFragment.newInstance()
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
