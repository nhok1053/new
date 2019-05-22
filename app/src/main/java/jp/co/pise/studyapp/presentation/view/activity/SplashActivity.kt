package jp.co.pise.studyapp.presentation.view.activity

import android.os.Bundle
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.data.repository.ICouponRepository
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.presentation.viewmodel.activity.SplashActivityViewModel
import java.util.*
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    internal lateinit var viewModel: SplashActivityViewModel

    @Inject
    internal lateinit var couponRepository: ICouponRepository

    private val SPLASH_DISPLAY_MS = 1000
    private var start: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        this.viewModel.addBug(this.subscriptions)

        couponRepository.getCoupon().observeOn(AndroidSchedulers.mainThread()).subscribe()
    }
}
