package jp.co.pise.study.framework.dagger

import dagger.Module
import jp.co.pise.study.presentation.App
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: App) = application.applicationContext
}