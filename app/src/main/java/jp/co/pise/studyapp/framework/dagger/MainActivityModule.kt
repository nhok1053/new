package jp.co.pise.studyapp.framework.dagger

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.co.pise.studyapp.presentation.view.fragment.CouponFragment

@Module
internal abstract class MainActivityModule {
//    @ContributesAndroidInjector
//    internal abstract fun contributeNewsFragment(): NewsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeCouponFragment(): CouponFragment

//    @ContributesAndroidInjector
//    internal abstract fun contributeProductFragment(): ProductFragment
}