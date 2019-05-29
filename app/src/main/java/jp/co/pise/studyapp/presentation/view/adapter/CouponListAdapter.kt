package jp.co.pise.studyapp.presentation.view.adapter

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.domain.model.GetCouponItemModel
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.presentation.viewmodel.adapter.CouponListItemViewModel
import jp.co.pise.studyapp.databinding.ItemCouponListBinding
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.extension.replaceObserve
import jp.co.pise.studyapp.extension.resizeFromDimen

class CouponListAdapter(viewModels: ObservableArrayList<CouponListItemViewModel>, owner: LifecycleOwner) : BaseAdapter<CouponListItemViewModel, CouponListAdapter.ViewHolder>(viewModels, owner) {
    init { this.viewModels.forEach { setCommand(it) } }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon_list, parent, false)
        return ViewHolder(view, owner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(this.viewModels[position])
    }

    override fun getItemCount(): Int {
        return this.viewModels.size
    }

    override fun onListItemRangeInserted(sender: ObservableList<CouponListItemViewModel>, positionStart: Int, itemCount: Int) {
        super.onListItemRangeInserted(sender, positionStart, itemCount)
        (positionStart until itemCount).forEach { index -> sender[index]?.let { vm -> this.setCommand(vm) } }
    }

    fun useCoupon(model: GetCouponItemModel) {
        this.viewModels.firstOrNull { it.id == model.id }?.useCoupon()
    }

    private fun setCommand(viewModel: CouponListItemViewModel) {
        viewModel.onUseCouponConfirm.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::itemClick) {}.addBug(this.subscriptions)
        viewModel.onLoginExpired.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::doLoginExpired) {}.addBug(this.subscriptions)
    }

    class ViewHolder(root: View, private val owner: LifecycleOwner) : RecyclerView.ViewHolder(root) {
        val binding =
                DataBindingUtil.bind<ItemCouponListBinding>(root)!!.owner(this.owner)

        init {
            this.binding.productPriceWithoutTax.paintFlags =
                    this.binding.productPriceWithoutTax.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            this.binding.productPriceInTax.paintFlags =
                    this.binding.productPriceWithoutTax.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        fun update(viewModel: CouponListItemViewModel) {
            this.binding.viewModel = viewModel
            viewModel.imageUrl.replaceObserve(this.owner, Observer { updateImage(it) })
        }

        private fun updateImage(imageUrl: String?) {
            try {
                if (!TextUtils.isEmpty(imageUrl)) {
                    this.binding.image.resizeFromDimen(
                            imageUrl,
                            R.dimen.coupon_image_width,
                            R.dimen.coupon_image_height)
                    this.binding.image.visibility = View.VISIBLE
                } else {
                    this.binding.image.visibility = View.INVISIBLE
                }
            } catch (e: Exception) {
                this.binding.image.visibility = View.INVISIBLE
            }
        }
    }
}
