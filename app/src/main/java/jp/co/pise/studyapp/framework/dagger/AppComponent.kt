package jp.co.pise.studyapp.framework.dagger

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import jp.co.pise.studyapp.presentation.App
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityModule::class,
    FrameworkModule::class,
    RepositoryModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    // application
    fun inject(application: App)

    // custom view
//    fun inject(drawerHeaderView: DrawerHeaderView)

    // view model
//    fun createCouponListItemViewModel(): CouponListItemViewModel
}