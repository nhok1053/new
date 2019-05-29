package jp.co.pise.studyapp.presentation.view.activity

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import jp.co.pise.studyapp.framework.rx.LoginExpiredMessage
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.lifecycle.DisposableObserver

abstract class BaseActivity : AppCompatActivity() {
    protected val disableObserver = DisposableObserver()
    protected val subscriptions: CompositeDisposable get() = disableObserver.subscriptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lifecycle.addObserver(this.disableObserver)
    }

    // region <----- private method ----->

    private fun translucentStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    // endregion

    // region <----- protected method ----->

    protected fun doLoginExpired(message: LoginExpiredMessage) = StudyApp.instance.doLoginExpired()
    open protected fun onLoginExpired() {
        Toast.makeText(this, jp.co.pise.studyapp.definition.Message.LOGIN_EXPIRED_ERROR, Toast.LENGTH_SHORT).show()
        val intent = LoginActivity.createIntent(this)
        startActivity(intent)
    }

    // endregion
}