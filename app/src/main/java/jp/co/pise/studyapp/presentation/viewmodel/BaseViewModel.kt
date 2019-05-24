package jp.co.pise.studyapp.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import jp.co.pise.studyapp.framework.rx.Messenger

abstract class BaseViewModel : ViewModel(), Disposable {
    protected val subscriptions: CompositeDisposable = CompositeDisposable()
    protected var messenger = Messenger()
        private set

    override fun dispose() {
        if (!subscriptions.isDisposed) subscriptions.dispose()
    }

    override fun isDisposed(): Boolean = subscriptions.isDisposed
}