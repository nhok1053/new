package jp.co.pise.study.presentation.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import jp.co.pise.study.R
import jp.co.pise.study.databinding.ActivitySecondBinding
import jp.co.pise.study.extension.addBug
import jp.co.pise.study.presentation.view.fragment.GalleryFragment
import jp.co.pise.study.presentation.view.fragment.ToolsFragment
import jp.co.pise.study.presentation.viewmodel.activity.SecondActivityViewModel
import javax.inject.Inject

private val TAG = "SecondActivity"

class SecondActivity : BaseActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var viewModel: SecondActivityViewModel

    private lateinit var binding: ActivitySecondBinding
    private var toolsFragment: ToolsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_second)
        this.viewModel.addBug(this.disableObserver.subscriptions)
        this.binding.viewModel = viewModel

        // Toolbar Setting
        (this.binding.toolbar as? Toolbar)?.let {
            toolbar ->
            {
                setSupportActionBar(toolbar)
                val toggle = ActionBarDrawerToggle(
                        this, this.binding.drawerLayout,
                        toolbar,
                        R.string.drawer_open,
                        R.string.drawer_close)
                this.binding.drawerLayout.addDrawerListener(toggle)
                toggle.syncState()
            }
        }
        this.binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_item1 -> Log.d(TAG, "Item 1 Selected!")
                R.id.menu_item2 -> Log.d(TAG, "Item 2 Selected!")
                R.id.menu_item3 -> Log.d(TAG, "Item 3 Selected!")
                R.id.menu_item4 -> Log.d(TAG, "Item 4 Selected!")
            }
            this.binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // BottomNavigation Setting
        this.binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_camera -> replaceContainer(GalleryFragment.newInstance())
                R.id.nav_gallery -> replaceContainer(GalleryFragment.newInstance())
                R.id.nav_slideshow -> Log.d(TAG, "Item 3 Selected!")
                R.id.nav_manage -> {
                    if (this.toolsFragment == null || this.binding.bottomNavigation.selectedItemId == R.id.nav_manage) {
                        this.toolsFragment = ToolsFragment.newInstance()
                    }
                    this.toolsFragment?.let { fragment -> replaceContainer(fragment) }
                }
            }
            true
        }
        replaceContainer(GalleryFragment.newInstance())
    }

    override fun supportFragmentInjector() = this.fragmentInjector

    private fun replaceContainer(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.container, fragment)
            commit()
        }
    }
}
