package jp.co.pise.studyapp.presentation.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DisposableObserver : LifecycleObserver {
    val subscriptions: CompositeDisposable = CompositeDisposable()

    fun addAll(vararg disposable: Disposable) = subscriptions.addAll(*disposable)

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopped() {
        if (!this.subscriptions.isDisposed) this.subscriptions.dispose()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyed() {
        if (!this.subscriptions.isDisposed) this.subscriptions.dispose()
    }
}