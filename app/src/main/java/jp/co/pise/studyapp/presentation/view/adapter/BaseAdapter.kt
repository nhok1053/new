package jp.co.pise.studyapp.presentation.view.adapter

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import jp.co.pise.studyapp.framework.rx.LoginExpiredMessage
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.viewmodel.BaseViewModel

abstract class BaseAdapter<VM : BaseViewModel, VH : RecyclerView.ViewHolder>(val viewModels: ObservableArrayList<VM>, val owner: LifecycleOwner, val context: Context) : RecyclerView.Adapter<VH>(), Disposable {
    private val onItemClickSubject: Subject<VM> = PublishSubject.create()
    protected val subscriptions = CompositeDisposable()

    private val onListChangedCallback = object : ObservableList.OnListChangedCallback<ObservableList<VM>>() {
        override fun onChanged(sender: ObservableList<VM>) {
            onListItemChange(sender)
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(sender: ObservableList<VM>, positionStart: Int, itemCount: Int) {
            onListItemRangeChanged(sender, positionStart, itemCount)
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(sender: ObservableList<VM>, positionStart: Int, itemCount: Int) {
            onListItemRangeInserted(sender, positionStart, itemCount)
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableList<VM>, fromPosition: Int, toPosition: Int, itemCount: Int) {
            onListItemRangeMoved(sender, fromPosition, toPosition, itemCount)
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeRemoved(sender: ObservableList<VM>, positionStart: Int, itemCount: Int) {
            onListItemRangeRemoved(sender, positionStart, itemCount)
            notifyItemRangeRemoved(positionStart, itemCount)
        }
    }

    init {
        this.viewModels.addOnListChangedCallback(onListChangedCallback)
    }

    fun onItemClick(): Observable<VM> {
        return this.onItemClickSubject
    }

    protected fun loginExpired(message: LoginExpiredMessage) = StudyApp.instance.sendLoginExpired()
    protected fun itemClick(viewModel: VM) = this.onItemClickSubject.onNext(viewModel)

    protected open fun onListItemChange(sender: ObservableList<VM>) {}
    protected open fun onListItemRangeChanged(sender: ObservableList<VM>, positionStart: Int, itemCount: Int) {}
    protected open fun onListItemRangeInserted(sender: ObservableList<VM>, positionStart: Int, itemCount: Int) {}
    protected open fun onListItemRangeMoved(sender: ObservableList<VM>, fromPosition: Int, toPosition: Int, itemCount: Int) {}
    protected open fun onListItemRangeRemoved(sender: ObservableList<VM>, positionStart: Int, itemCount: Int) {}
}
