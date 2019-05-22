package jp.co.pise.studyapp.extension

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addBug(subscriptions: CompositeDisposable) {
    subscriptions.add(this)
}