package jp.co.pise.studyapp.presentation.view.fragment.tab

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.FragmentProductTabBinding
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.presentation.view.fragment.BaseTabFragment
import jp.co.pise.studyapp.presentation.view.fragment.child.ProductFragment
import jp.co.pise.studyapp.presentation.viewmodel.fragment.tab.ProductTabFragmentViewModel
import javax.inject.Inject

class ProductTabFragment : BaseTabFragment() {
    @Inject
    lateinit var viewModel: ProductTabFragmentViewModel
    private lateinit var binding: FragmentProductTabBinding

    companion object {
        const val TAG = "ProductTabFragment"

        fun newInstance(): ProductTabFragment {
            return ProductTabFragment()
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
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_tab, container, false)
        this.binding.viewModel = this.viewModel
        this.viewModel.addBug(this.subscriptions)

        // setting first fragment
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.container, createProductFragment(), ProductFragment.TAG)
        transaction.commit()

        return this.binding.root
    }

    private fun createProductFragment(): ProductFragment {
        return ProductFragment.newInstance()
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
