package jp.co.pise.studyapp.presentation.view.activity

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.definition.Message
import jp.co.pise.studyapp.definition.ResultCode
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.replaceObserve
import jp.co.pise.studyapp.extension.unwrap
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.viewmodel.activity.SplashActivityViewModel
import java.util.*
import javax.inject.Inject

private const val SPLASH_DISPLAY_MS = 1000

class SplashActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: SplashActivityViewModel
    private lateinit var start: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // setting view model
        this.viewModel.addBug(this.subscriptions)

        // ログイン状態初期化
        StudyApp.instance.logout()

        // 表示開始時刻
        this.start = Date()

        subscribe()
        this.viewModel.autoLogin()
    }

    private fun subscribe() {
        this.viewModel.onLogin.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    StudyApp.instance.login(it)
                    Toast.makeText(this, Message.LOGIN, Toast.LENGTH_SHORT).show()
                }, { }).addBug(this.subscriptions)
        this.viewModel.onLoginError.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    StudyApp.instance.logout()
                    if (it.resultCode == ResultCode.UserNotLogin)
                        Toast.makeText(this, Message.LOGIN_PROMOTION, Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this, Message.AUTO_LOGIN_FAILED, Toast.LENGTH_SHORT).show()
                }, { }).addBug(this.subscriptions)

        this.viewModel.isLoading.replaceObserve(this, android.arch.lifecycle.Observer {
            if (!it.unwrap) {
                // 差分の秒を算出します。
                val diffMs = Date().time - start.time
                val delay = SPLASH_DISPLAY_MS - diffMs

                if (delay > 0) {
                    Handler().postDelayed({ transNextActivity() }, SPLASH_DISPLAY_MS.toLong())
                } else {
                    transNextActivity()
                }
            }
        })
    }

    private fun transNextActivity() {
        val intent = MainActivity.createIntent(this)
        startActivity(intent)
        finish()
    }
}
