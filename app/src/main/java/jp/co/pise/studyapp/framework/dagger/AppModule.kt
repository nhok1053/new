package jp.co.pise.studyapp.framework.dagger

import android.content.Context
import dagger.Module
import jp.co.pise.studyapp.presentation.StudyApp
import dagger.Provides
import javax.inject.Singleton


@Module
internal class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: StudyApp): Context {
        return application.applicationContext
    }
}