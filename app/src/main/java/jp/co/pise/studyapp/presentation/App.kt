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
import javax.inject.Inject

class App : Application(), HasActivityInjector, HasSupportFragmentInjector {
    companion object {
        lateinit var instance: App private set
    }

    private var loginUser: LoginUser? = null
    //    private val messenger = Messenger()

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    lateinit var daggerAppComponent: AppComponent private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeDaggerComponent()
        initializeFresco()
    }

    // region <----- initializer ----->

    private fun initializeDaggerComponent() {
        this.daggerAppComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build()
        this.daggerAppComponent.inject(this)
    }

    private fun initializeFresco() {
        val config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build()
        Fresco.initialize(this, config)
    }

    // endregion

    override fun activityInjector(): AndroidInjector<Activity> {
        return this.activityInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return this.fragmentInjector
    }
}