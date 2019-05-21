package jp.co.pise.study.domain.usecase

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class Usecase : Disposable {
    private var disposables: CompositeDisposable = CompositeDisposable()

    override fun dispose() {
        if (!disposables.isDisposed) disposables.dispose()
    }

    override fun isDisposed(): Boolean = disposables.isDisposed
}