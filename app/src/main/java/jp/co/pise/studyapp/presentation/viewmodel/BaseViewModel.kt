package jp.co.pise.studyapp.presentation.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel(), Disposable {
    private val subscriptions: CompositeDisposable = CompositeDisposable()

    override fun dispose() {
        if (!subscriptions.isDisposed) subscriptions.dispose()
    }

    override fun isDisposed(): Boolean = subscriptions.isDisposed
}