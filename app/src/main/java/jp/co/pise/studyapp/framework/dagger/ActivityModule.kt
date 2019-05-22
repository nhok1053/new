package jp.co.pise.studyapp.framework.dagger

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.co.pise.studyapp.presentation.view.activity.SplashActivity

@Module
internal abstract class ActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeSplashActivity(): SplashActivity

//    @ContributesAndroidInjector
//    internal abstract fun contributeLoginActivity(): LoginActivity
//
//    @ContributesAndroidInjector(modules = [MainActivityModule::class])
//    internal abstract fun contributeMainActivity(): MainActivity
}
