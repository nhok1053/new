package jp.co.pise.studyapp.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.ActivityMainBinding
import jp.co.pise.studyapp.definition.Message
import jp.co.pise.studyapp.domain.model.GetCouponItemModel
import jp.co.pise.studyapp.domain.model.ProductItemModel
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.view.customview.DrawerHeaderView
import jp.co.pise.studyapp.presentation.view.fragment.CouponFragment
import jp.co.pise.studyapp.presentation.view.fragment.NewsFragment
import jp.co.pise.studyapp.presentation.view.fragment.ProductFragment
import jp.co.pise.studyapp.presentation.view.fragment.dialog.CouponUseFragment
import jp.co.pise.studyapp.presentation.view.fragment.dialog.ProductDetailFragment
import jp.co.pise.studyapp.presentation.viewmodel.activity.MainActivityViewModel
import javax.inject.Inject

class MainActivity : BaseActivity(),
        HasSupportFragmentInjector,
        CouponFragment.UseCouponConfirmListener,
        CouponUseFragment.UseCouponListener,
        ProductFragment.ShowProductDetailListener {

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
        this.binding = DataBindingUtil
                .setContentView<ActivityMainBinding>(this, R.layout.activity_main)
                .owner(this)
        this.binding.viewModel = viewModel
        this.viewModel.addBug(this.subscriptions)

        this.viewModel.onLogout.observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    StudyApp.instance.logout()
                    Toast.makeText(this, Message.LOGOUT, Toast.LENGTH_SHORT).show()
                }, { }).addBug(this.subscriptions)
        this.viewModel.onRefreshUsedCoupon.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isSuccess ->
                    if (isSuccess) {
                        StudyApp.instance.sendRefreshedUsedCoupon()
                        Toast.makeText(this, Message.REFRESH_USED_COUPON_SUCCESS, Toast.LENGTH_SHORT).show()
                    }
                }, { }).addBug(this.subscriptions)
        this.viewModel.onLoginExpired.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::loginExpired) { }.addBug(this.subscriptions)
        StudyApp.instance.onLoginStateChange.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ loginStateChanged() }, { }).addBug(this.subscriptions)
        StudyApp.instance.onLoginExpired.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onLoginExpired() }, { }).addBug(this.subscriptions)

        // Drawer Setting
        if (this.binding.navigationView.headerCount > 0) {
            // Headerが存在する場合全てRemoveする
            for (index in this.binding.navigationView.headerCount - 1 downTo 0) {
                this.binding.navigationView.removeHeaderView(this.binding.navigationView.getHeaderView(index))
            }
        }
        this.drawerHeaderView = DrawerHeaderView(this, this)
        this.binding.navigationView.addHeaderView(this.drawerHeaderView)
        settingDrawerHeader()
        settingDrawerMenu()
        this.drawerHeaderView.addBug(this.subscriptions)

        // Toolbar Setting
        (this.binding.toolbar as Toolbar?)?.let { toolbar ->
            setSupportActionBar(toolbar)

            val toggle = ActionBarDrawerToggle(
                    this, this.binding.drawerLayout,
                    toolbar,
                    R.string.drawer_open,
                    R.string.drawer_close)
            this.binding.drawerLayout.addDrawerListener(toggle)
            this.binding.drawerLayout.setScrimColor(ContextCompat.getColor(this, R.color.menuScrim))
            this.binding.navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.usedCoupon -> {
                    }

                    R.id.privacyPolicy -> {
                    }

                    R.id.licenses -> {
                    }

                    R.id.login -> startActivity(LoginActivity.createIntent(this))

                    R.id.logout -> this.viewModel.logout()

                    R.id.refreshCoupon -> if (StudyApp.instance.isLoggedIn && StudyApp.instance.loginUser != null)
                        this.viewModel.refreshUsedCoupon(StudyApp.instance.loginUser!!)
                }
                this.binding.drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            toggle.syncState()
        }

        // BottomNavigation Setting
        this.binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_news -> replaceNewsFragment()
                R.id.nav_coupon -> replaceCouponFragment()
                R.id.nav_product -> replaceProductFragment()
            }
            true
        }

        // 再生成でない場合のみ初期選択にする
        if (savedInstanceState == null)
            replaceNewsFragment()
    }

    // region <----- private method ----->

    private fun loginStateChanged() {
        settingDrawerHeader()
        settingDrawerMenu()
    }

    private fun settingDrawerHeader() {
        if (StudyApp.instance.isLoggedIn && StudyApp.instance.loginUser != null) {
            this.drawerHeaderView.setLogin(true, StudyApp.instance.loginUser)
        } else {
            this.drawerHeaderView.setLogin(false, null)
        }
    }

    private fun settingDrawerMenu() {
        if (this.binding.navigationView.menu != null)
            this.binding.navigationView.menu.clear()

        if (StudyApp.instance.isLoggedIn) {
            this.binding.navigationView.inflateMenu(R.menu.signin_drawer_item)
        } else {
            this.binding.navigationView.inflateMenu(R.menu.not_signin_drawer_item)
        }
    }

    private fun replaceNewsFragment() {
        replaceContainer(createNewsFragment(), NewsFragment.TAG, resources.getString(R.string.news_tab_title))
    }

    private fun replaceCouponFragment() {
        replaceContainer(createCouponFragment(), CouponFragment.TAG, resources.getString(R.string.coupon_tab_title))
    }

    private fun replaceProductFragment() {
        replaceContainer(createProductFragment(), ProductFragment.TAG, resources.getString(R.string.product_tab_title))
    }

    private fun createNewsFragment(): NewsFragment {
        return NewsFragment.newInstance()
    }

    private fun createCouponFragment(): CouponFragment {
        return CouponFragment.newInstance()
    }

    private fun createProductFragment(): ProductFragment {
        return ProductFragment.newInstance()
    }

    private fun replaceContainer(fragment: Fragment, tag: String, title: String) {
        if (supportActionBar != null)
            supportActionBar!!.title = title

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment, tag)
        transaction.commit()
    }

    // endregion

    // region <----- interface ----->

    override fun onUsedCouponConfirm(model: GetCouponItemModel) {
        // Fragmentの表示をシングルトンにする為、既に表示されていた場合は閉じる
        var fragment = supportFragmentManager.findFragmentByTag(CouponUseFragment.TAG) as CouponUseFragment?
        fragment?.dismiss()

        fragment = CouponUseFragment.newInstance(model)
        fragment.show(supportFragmentManager, CouponUseFragment.TAG)
    }

    override fun onUseCoupon(model: GetCouponItemModel) {
        (supportFragmentManager.findFragmentByTag(CouponFragment.TAG) as CouponFragment?)?.useCoupon(model)
    }

    override fun onShowProductDetail(model: ProductItemModel) {
        // Fragmentの表示をシングルトンにする為、既に表示されていた場合は閉じる
        var fragment = supportFragmentManager.findFragmentByTag(ProductDetailFragment.TAG) as ProductDetailFragment?
        fragment?.dismiss()

        fragment = ProductDetailFragment.newInstance(model)
        fragment.show(supportFragmentManager, ProductDetailFragment.TAG)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return this.fragmentInjector
    }

    // endregion
}
