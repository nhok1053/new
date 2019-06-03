package jp.co.pise.studyapp.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.ActivityMainBinding
import jp.co.pise.studyapp.definition.Message
import jp.co.pise.studyapp.domain.model.LoginUser
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.framework.rx.LoginStateChangeMessage
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.view.customview.DrawerHeaderView
import jp.co.pise.studyapp.presentation.view.fragment.BaseFragment
import jp.co.pise.studyapp.presentation.view.fragment.BaseTabFragment
import jp.co.pise.studyapp.presentation.view.fragment.tab.CouponTabFragment
import jp.co.pise.studyapp.presentation.view.fragment.tab.NewsTabFragment
import jp.co.pise.studyapp.presentation.view.fragment.tab.ProductTabFragment
import jp.co.pise.studyapp.presentation.viewmodel.activity.MainActivityViewModel
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var viewModel: MainActivityViewModel

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerHeaderView: DrawerHeaderView

    companion object {
        const val TAG = "MainActivity"

        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        // setting binding
        this.binding = DataBindingUtil
                .setContentView<ActivityMainBinding>(this, R.layout.activity_main)
                .owner(this)
        this.binding.viewModel = viewModel
        this.viewModel.addBug(this.subscriptions)

        // setting view model message
        this.viewModel.onLogout.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ doLogout() }, { }).addBug(this.subscriptions)
        this.viewModel.onRefreshUsedCoupon.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::doRefreshedUsedCoupon) { }.addBug(this.subscriptions)
        this.viewModel.onLoginExpired.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::doLoginExpired) { }.addBug(this.subscriptions)

        // setting app message
        StudyApp.instance.onLoginStateChange.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoginStateChanged) { }.addBug(this.subscriptions)
        StudyApp.instance.onLoginExpired.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onLoginExpired() }, { }).addBug(this.subscriptions)

        // setting drawer
        if (this.binding.navigationView.headerCount > 0) {
            // Headerが存在する場合全てRemoveする
            for (index in this.binding.navigationView.headerCount - 1 downTo 0) {
                this.binding.navigationView.removeHeaderView(this.binding.navigationView.getHeaderView(index))
            }
        }
        this.drawerHeaderView = DrawerHeaderView(this, this)
        this.binding.navigationView.addHeaderView(this.drawerHeaderView)
        settingDrawerMenu()
        this.drawerHeaderView.addBug(this.subscriptions)

        // setting toolbar
        (this.binding.toolbar as Toolbar?)?.let { toolbar ->
            setSupportActionBar(toolbar)

            val toggle = object : ActionBarDrawerToggle(
                    this, this.binding.drawerLayout,
                    toolbar,
                    R.string.drawer_open,
                    R.string.drawer_close) {

                override fun onDrawerStateChanged(newState: Int) {
                    // ドラッグ開始、もしくはアニメーション開始（アイコンタップ時）の場合Dialogを閉じる
                    if (newState == DrawerLayout.STATE_DRAGGING || newState == DrawerLayout.STATE_SETTLING)
                        dismissDialogFragment()

                    super.onDrawerStateChanged(newState)
                }
            }
            this.binding.drawerLayout.addDrawerListener(toggle)
            this.binding.drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.menuScrim))
            this.binding.navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.usedCoupon -> { }
                    R.id.privacyPolicy -> { }
                    R.id.licenses -> { }
                    R.id.login -> startActivity(LoginActivity.createIntent(this))
                    R.id.logout -> this.viewModel.logout()
                    R.id.refreshCoupon ->
                        if (StudyApp.instance.isLoggedIn && StudyApp.instance.loginUser != null)
                            this.viewModel.refreshUsedCoupon(StudyApp.instance.loginUser!!)
                }
                this.binding.drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            toggle.syncState()
        }

        // setting bottom navigation
        this.binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_news -> showNewsTab()
                R.id.nav_coupon -> showCouponTab()
                R.id.nav_product -> showProductTab()
            }
            true
        }

        // 再生成でない場合のみ初期設定する
        if (savedInstanceState == null) {
            this.supportFragmentManager.beginTransaction()
                    .add(R.id.container, createNewsTabFragment(), NewsTabFragment.TAG)
                    .add(R.id.container, createCouponTabFragment(), CouponTabFragment.TAG)
                    .add(R.id.container, createProductTabFragment(), ProductTabFragment.TAG)
                    .commitNow()

            showNewsTab()
        }
    }

    // region <----- private method ----->

    private fun doLogout() {
        StudyApp.instance.logout()
        Toast.makeText(this, Message.LOGOUT, Toast.LENGTH_SHORT).show()
    }

    private fun doRefreshedUsedCoupon(isSuccess: Boolean) {
        if (isSuccess) {
            StudyApp.instance.doRefreshedUsedCoupon()
            Toast.makeText(this, Message.REFRESH_USED_COUPON_SUCCESS, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, Message.REFRESH_USED_COUPON_FAILED, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onLoginStateChanged(message: LoginStateChangeMessage) {
        settingDrawerMenu(message.isLogin, message.loginUser)
    }

    override fun onLoginExpired() {
        dismissDialogFragment()
        super.onLoginExpired()
    }

    private fun settingDrawerMenu() {
        settingDrawerMenu(StudyApp.instance.isLoggedIn, StudyApp.instance.loginUser)
    }

    private fun settingDrawerMenu(isLogin: Boolean, loginUser: LoginUser?) {
        if (this.binding.navigationView.menu != null)
            this.binding.navigationView.menu.clear()

        if (isLogin && loginUser != null) {
            this.binding.navigationView.inflateMenu(R.menu.login_drawer_item)
        } else {
            this.binding.navigationView.inflateMenu(R.menu.not_login_drawer_item)
        }
    }

    private fun showNewsTab() {
        dismissDialogFragment()

        if (supportActionBar != null)
            supportActionBar!!.title = resources.getString(R.string.news_tab_title)

        showFragment<NewsTabFragment>()
    }

    private fun showCouponTab() {
        dismissDialogFragment()

        if (supportActionBar != null)
            supportActionBar!!.title = resources.getString(R.string.coupon_tab_title)

        showFragment<CouponTabFragment>()
    }

    private fun showProductTab() {
        dismissDialogFragment()

        if (supportActionBar != null)
            supportActionBar!!.title = resources.getString(R.string.product_tab_title)

        showFragment<ProductTabFragment>()
    }

    private inline fun <reified T : BaseFragment> showFragment() {
        supportFragmentManager.executePendingTransactions()
        this.supportFragmentManager.beginTransaction().apply {
            supportFragmentManager.fragments.forEach {
                if (it is T) show(it)
                else hide(it)
            }
            commitNow()
        }
    }

    private fun createNewsTabFragment(): NewsTabFragment {
        return NewsTabFragment.newInstance()
    }

    private fun createCouponTabFragment(): CouponTabFragment {
        return CouponTabFragment.newInstance()
    }

    private fun createProductTabFragment(): ProductTabFragment {
        return ProductTabFragment.newInstance()
    }

    private fun dismissDialogFragment() {
        supportFragmentManager.executePendingTransactions()
        supportFragmentManager.fragments.forEach { if (it is DialogFragment) it.dismiss() }

        supportFragmentManager.fragments
                .filter { it is BaseTabFragment }
                .map { it as BaseTabFragment }
                .forEach(BaseTabFragment::dismissDialogFragment)
    }

    // endregion

    // region <----- interface ----->

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return this.fragmentInjector
    }

    override fun onBackPressed() {
        val isPopBack = supportFragmentManager.fragments
                .filter { it is BaseTabFragment && !it.isHidden }
                .map { it as BaseTabFragment }
                .firstOrNull()?.popBackStack() ?: false

        if (!isPopBack)
            super.onBackPressed()
    }

    // endregion
}
