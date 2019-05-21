package jp.co.pise.study.framework.dagger

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.co.pise.study.presentation.view.activity.MainActivity
import jp.co.pise.study.presentation.view.activity.SecondActivity

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [ SecondActivityModule::class ])
    abstract fun contributeSecondActivity(): SecondActivity
}