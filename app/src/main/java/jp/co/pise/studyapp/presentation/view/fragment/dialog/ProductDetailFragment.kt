package jp.co.pise.studyapp.presentation.view.fragment.dialog

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.FragmentProductDetailBinding
import jp.co.pise.studyapp.domain.model.ProductItemModel
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.extension.replaceObserve
import jp.co.pise.studyapp.extension.resizeFromDimen
import jp.co.pise.studyapp.presentation.viewmodel.fragment.dialog.ProductDetailFragmentViewModel

private const val PRODUCT = "product"

class ProductDetailFragment : BaseDialogFragment() {
    private var viewModel: ProductDetailFragmentViewModel? = null
    private lateinit var binding: FragmentProductDetailBinding

    companion object {
        const val TAG = "ProductDetailFragment"

        fun newInstance(model: ProductItemModel): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            val args = Bundle()
            args.putSerializable(PRODUCT, model)
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
        this.viewModel = (arguments?.getSerializable(PRODUCT) as ProductItemModel?)
                ?.let { model -> ProductDetailFragmentViewModel.fromResultItem(model) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.binding = DataBindingUtil
                .inflate<FragmentProductDetailBinding>(inflater, R.layout.fragment_product_detail, container, false)
                .owner(this)

        this.viewModel?.let {
            it.imageUrl.replaceObserve(this, Observer<String>(this::setImage))
            it.onClose.observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ dismiss() }, { }).addBug(this.subscriptions)

            this.binding.viewModel = this.viewModel
            it.addBug(this.subscriptions)
        }

        return this.binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
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
                        R.dimen.product_detail_image_width,
                        R.dimen.product_detail_image_height)
                this.binding.image.visibility = View.VISIBLE
            } else {
                this.binding.image.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {
            this.binding.image.visibility = View.INVISIBLE
        }
    }
}
