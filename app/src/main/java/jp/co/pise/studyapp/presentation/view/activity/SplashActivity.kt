package jp.co.pise.studyapp.presentation.view.activity

import android.os.Bundle
import dagger.android.AndroidInjection
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.presentation.viewmodel.activity.SplashActivityViewModel
import java.util.*
import javax.inject.Inject

class SplashActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: SplashActivityViewModel

    private val SPLASH_DISPLAY_MS = 1000
    private var start: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        this.viewModel.addBug(this.subscriptions)
    }
}
