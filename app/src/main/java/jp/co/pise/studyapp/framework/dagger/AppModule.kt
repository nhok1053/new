package jp.co.pise.studyapp.framework.dagger

import android.content.Context
import dagger.Module
import jp.co.pise.studyapp.presentation.App
import dagger.Provides
import javax.inject.Singleton


@Module
internal class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: App): Context {
        return application.applicationContext
    }
}