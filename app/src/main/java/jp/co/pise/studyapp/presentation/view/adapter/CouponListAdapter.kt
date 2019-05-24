package jp.co.pise.studyapp.presentation.view.adapter

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
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
import jp.co.pise.studyapp.extension.resizeFromDimen

class CouponListAdapter(viewModels: ObservableArrayList<CouponListItemViewModel>, owner: LifecycleOwner, context: Context) : BaseAdapter<CouponListItemViewModel, CouponListAdapter.ViewHolder>(viewModels, owner, context) {
    init { this.viewModels.forEach { setCommand(it) } }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_coupon_list, parent, false)
        return ViewHolder(view, owner, this.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(this.viewModels[position])
    }

    override fun getItemCount(): Int {
        return this.viewModels.size
    }

    override fun onListItemRangeInserted(sender: ObservableList<CouponListItemViewModel>, positionStart: Int, itemCount: Int) {
        super.onListItemRangeInserted(sender, positionStart, itemCount)
        (positionStart until itemCount).forEach { sender[it]?.apply { setCommand(this) } }
    }

    override fun dispose() {
        if (!this.subscriptions.isDisposed)
            this.subscriptions.dispose()
    }

    override fun isDisposed(): Boolean {
        return this.subscriptions.isDisposed
    }

    fun useCoupon(model: GetCouponItemModel) {
        this.viewModels.firstOrNull { it.id == model.id }?.useCoupon()
    }

    private fun setCommand(viewModel: CouponListItemViewModel) {
        viewModel.onUseCouponComfirm.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::itemClick) {}.addBug(this.subscriptions)
        viewModel.onLoginExpired.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loginExpired) {}.addBug(this.subscriptions)
    }

    class ViewHolder(view: View, owner: LifecycleOwner, val context: Context) : RecyclerView.ViewHolder(view) {
        private val binding: ItemCouponListBinding = DataBindingUtil.bind(view)!!

        init {
            this.binding.lifecycleOwner = owner

            this.binding.productPriceWithoutTax.paintFlags =
                    this.binding.productPriceWithoutTax.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            this.binding.productPriceInTax.paintFlags =
                    this.binding.productPriceWithoutTax.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        fun update(viewModel: CouponListItemViewModel) {
            this.binding.viewModel = viewModel
            updateImage(viewModel)
        }

        private fun updateImage(viewModel: CouponListItemViewModel) {
            try {
                if (!TextUtils.isEmpty(viewModel.imageUrl.value)) {
                    this.binding.image.resizeFromDimen(this.context,
                            viewModel.imageUrl.value!!,
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
