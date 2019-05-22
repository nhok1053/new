package jp.co.pise.studyapp.presentation.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import jp.co.pise.studyapp.presentation.lifecycle.DisposableObserver

abstract class BaseActivity : AppCompatActivity() {
    protected val disableObserver = DisposableObserver()
    protected val subscriptions: CompositeDisposable get() = disableObserver.subscriptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lifecycle.addObserver(this.disableObserver)
    }
}