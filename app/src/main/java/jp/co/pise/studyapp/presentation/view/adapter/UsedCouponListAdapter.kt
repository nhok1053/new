package jp.co.pise.studyapp.presentation.view.adapter

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.ItemUsedCouponListBinding
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.presentation.viewmodel.adapter.UsedCouponListItemViewModel

class UsedCouponListAdapter(viewModels: ObservableArrayList<UsedCouponListItemViewModel>, owner: LifecycleOwner) : BaseAdapter<UsedCouponListItemViewModel, UsedCouponListAdapter.ViewHolder>(viewModels, owner) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_used_coupon_list, parent, false)
        return ViewHolder(view, owner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.update(this.viewModels[position])
    }

    override fun getItemCount(): Int {
        return this.viewModels.size
    }

    override fun dispose() {
        if (!this.subscriptions.isDisposed)
            this.subscriptions.dispose()
    }

    override fun isDisposed(): Boolean {
        return this.subscriptions.isDisposed
    }

    class ViewHolder(view: View, owner: LifecycleOwner) : RecyclerView.ViewHolder(view) {
        private val binding: ItemUsedCouponListBinding =
                DataBindingUtil.bind<ItemUsedCouponListBinding>(view)!!.owner(owner)

        fun update(viewModel: UsedCouponListItemViewModel) {
            this.binding.viewModel = viewModel
        }
    }
}