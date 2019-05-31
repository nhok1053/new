package jp.co.pise.studyapp.framework.dagger

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.co.pise.studyapp.presentation.view.fragment.child.CouponFragment
import jp.co.pise.studyapp.presentation.view.fragment.child.NewsDetailFragment
import jp.co.pise.studyapp.presentation.view.fragment.child.NewsFragment
import jp.co.pise.studyapp.presentation.view.fragment.child.ProductFragment
import jp.co.pise.studyapp.presentation.view.fragment.tab.CouponTabFragment
import jp.co.pise.studyapp.presentation.view.fragment.tab.NewsTabFragment
import jp.co.pise.studyapp.presentation.view.fragment.tab.ProductTabFragment

@Module
internal abstract class MainActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeNewsTabFragment(): NewsTabFragment

    @ContributesAndroidInjector
    internal abstract fun contributeNewsFragment(): NewsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeNewsDetailFragment(): NewsDetailFragment

    @ContributesAndroidInjector
    internal abstract fun contributeCouponTabFragment(): CouponTabFragment

    @ContributesAndroidInjector
    internal abstract fun contributeCouponFragment(): CouponFragment

    @ContributesAndroidInjector
    internal abstract fun contributeProductTabFragment(): ProductTabFragment

    @ContributesAndroidInjector
    internal abstract fun contributeProductFragment(): ProductFragment
}