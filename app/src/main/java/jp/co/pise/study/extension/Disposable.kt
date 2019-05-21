package jp.co.pise.study.extension

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addBug(subscriptions: CompositeDisposable) {
    subscriptions.add(this)
}