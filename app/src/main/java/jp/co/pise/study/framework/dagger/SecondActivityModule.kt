package jp.co.pise.study.framework.dagger

import dagger.Module
import dagger.android.ContributesAndroidInjector
import jp.co.pise.study.presentation.view.fragment.GalleryFragment
import jp.co.pise.study.presentation.view.fragment.ToolsFragment

@Module
abstract class SecondActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeGalleryFragment(): GalleryFragment

    @ContributesAndroidInjector
    abstract fun contributeToolsFragment(): ToolsFragment
}