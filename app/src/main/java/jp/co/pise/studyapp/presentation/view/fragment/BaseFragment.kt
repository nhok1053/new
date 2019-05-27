package jp.co.pise.studyapp.presentation.view.fragment

import android.support.v4.app.Fragment
import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import jp.co.pise.studyapp.framework.rx.LoginExpiredMessage
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.lifecycle.DisposableObserver

abstract class BaseFragment : Fragment() {
    private val disableObserver = DisposableObserver()
    protected val subscriptions get() = this.disableObserver.subscriptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lifecycle.addObserver(this.disableObserver)
    }

    protected fun loginExpired(message: LoginExpiredMessage) {
        StudyApp.instance.sendLoginExpired()
    }
}