package jp.co.pise.studyapp.presentation.view.fragment.dialog

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import io.reactivex.disposables.Disposable
import jp.co.pise.studyapp.framework.rx.LoginExpiredMessage
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.lifecycle.DisposableObserver

abstract class BaseDialogFragment : DialogFragment() {
    private val disableObserver = DisposableObserver()
    protected val subscriptions get() = this.disableObserver.subscriptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lifecycle.addObserver(this.disableObserver)
    }

    protected fun doLoginExpired(message: LoginExpiredMessage) = StudyApp.instance.doLoginExpired()
}
