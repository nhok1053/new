package jp.co.pise.studyapp.domain.usecase

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class Usecase : Disposable {
    protected var subscriptions: CompositeDisposable = CompositeDisposable()

    override fun dispose() {
        if (!subscriptions.isDisposed) subscriptions.dispose()
    }

    override fun isDisposed(): Boolean = subscriptions.isDisposed
}