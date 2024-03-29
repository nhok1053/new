package jp.co.pise.studyapp.framework.dagger

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.view.customview.DrawerHeaderView
import jp.co.pise.studyapp.presentation.viewmodel.adapter.CouponListItemViewModel
import jp.co.pise.studyapp.presentation.viewmodel.adapter.NewsListItemViewModel
import jp.co.pise.studyapp.presentation.viewmodel.adapter.ProductListItemViewModel
import jp.co.pise.studyapp.presentation.viewmodel.adapter.UsedCouponListItemViewModel
import jp.co.pise.studyapp.presentation.viewmodel.fragment.child.NewsDetailFragmentViewModel
import jp.co.pise.studyapp.presentation.viewmodel.fragment.dialog.CouponUseFragmentViewModel
import jp.co.pise.studyapp.presentation.viewmodel.fragment.dialog.ProductDetailFragmentViewModel
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
        fun application(application: StudyApp): Builder

        fun build(): AppComponent
    }

    // application
    fun inject(application: StudyApp)

    // custom view
    fun inject(drawerHeaderView: DrawerHeaderView)

    // view model
    fun createCouponListItemViewModel(): CouponListItemViewModel
    fun createNewsListItemViewModel(): NewsListItemViewModel
    fun createProductListItemViewModel(): ProductListItemViewModel
    fun createUsedCouponListItemViewModel(): UsedCouponListItemViewModel
    fun createProductDetailFragmentViewModel(): ProductDetailFragmentViewModel
    fun createCouponUseFragmentViewModel(): CouponUseFragmentViewModel
    fun createNewsDetailFragmentViewModel(): NewsDetailFragmentViewModel
}