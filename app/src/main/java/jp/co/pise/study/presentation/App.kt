package jp.co.pise.study.presentation

import dagger.android.support.DaggerApplication
import jp.co.pise.study.framework.dagger.DaggerAppComponent

class App : DaggerApplication() {
    override fun applicationInjector()
            = DaggerAppComponent.builder()
            .create(this)
}