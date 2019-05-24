package jp.co.pise.studyapp.presentation

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import jp.co.pise.studyapp.domain.model.LoginUser
import jp.co.pise.studyapp.framework.dagger.AppComponent
import jp.co.pise.studyapp.framework.dagger.DaggerAppComponent
import jp.co.pise.studyapp.framework.rx.LoginExpiredMessage
import jp.co.pise.studyapp.framework.rx.Messenger
import jp.co.pise.studyapp.framework.rx.RefreshedUsedCouponMessage
import jp.co.pise.studyapp.framework.rx.UserLoginStateChangeMessage
import javax.inject.Inject

class StudyApp : Application(), HasActivityInjector, HasSupportFragmentInjector {
    companion object {
        lateinit var instance: StudyApp private set
    }

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    lateinit var appComponent: AppComponent private set
    private val messenger = Messenger()
    var loginUser: LoginUser? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeDaggerComponent()
        initializeFresco()
    }

    // region <----- initializer ----->

    private fun initializeDaggerComponent() {
        this.appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build()
        this.appComponent.inject(this)
    }

    private fun initializeFresco() {
        val config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build()
        Fresco.initialize(this, config)
    }

    // endregion

    // region <----- messenger ----->

    fun loginStateChange() = this.messenger.register(UserLoginStateChangeMessage::class.java)

    fun loginExpired() = this.messenger.register(LoginExpiredMessage::class.java)

    fun refreshedUsedCoupon() = this.messenger.register(RefreshedUsedCouponMessage::class.java)

    fun sendLoginExpired() = this.messenger.send(LoginExpiredMessage())

    fun sendRefreshedUsedCoupon() = this.messenger.send(RefreshedUsedCouponMessage())

    // endregion

    // region <----- user logged in ----->

    fun login(loginUser: LoginUser) {
        if (this.loginUser?.loginId != loginUser.loginId) {
            this.loginUser = loginUser
            this.messenger.send(UserLoginStateChangeMessage(true))
        }
    }

    fun logout() {
        if (this.loginUser != null) {
            this.loginUser = null
            this.messenger.send(UserLoginStateChangeMessage(false))
        }
    }

    fun isLogin(): Boolean {
        return this.loginUser != null
    }

    // endregion

    override fun activityInjector(): AndroidInjector<Activity> {
        return this.activityInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return this.fragmentInjector
    }
}