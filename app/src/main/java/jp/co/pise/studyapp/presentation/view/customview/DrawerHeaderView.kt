package jp.co.pise.studyapp.presentation.view.customview

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.DrawerHeaderBinding
import jp.co.pise.studyapp.domain.model.LoginUser
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.extension.replaceObserve
import jp.co.pise.studyapp.extension.resizeFromDimen
import jp.co.pise.studyapp.framework.rx.LoginStateChangeMessage
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.viewmodel.customview.DrawerHeaderViewModel
import javax.inject.Inject

@SuppressLint("ViewConstructor")
class DrawerHeaderView(context: Context, owner: LifecycleOwner) : FrameLayout(context), Disposable {
    @Inject
    lateinit var viewModel: DrawerHeaderViewModel
    private val binding: DrawerHeaderBinding
    private val subscriptions = CompositeDisposable()

    init {
        StudyApp.instance.appComponent.inject(this)

        // setting binding
        this.binding = DataBindingUtil
                .inflate<DrawerHeaderBinding>(LayoutInflater.from(context), R.layout.drawer_header, this, true)
                .owner(owner)
        this.binding.viewModel = this.viewModel
        this.viewModel.addBug(this.subscriptions)

        // setting view model
        this.viewModel.imageUrl.replaceObserve(owner, Observer { updateImage(it) })

        // setting app message
        StudyApp.instance.onLoginStateChange.observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLoginStateChanged) {}.addBug(this.subscriptions)

        setLoginState()
    }

    private fun onLoginStateChanged(message: LoginStateChangeMessage) {
        setLoginState(message.isLogin, message.loginUser)
    }

    private fun setLoginState() {
        setLoginState(StudyApp.instance.isLoggedIn, StudyApp.instance.loginUser)
    }

    private fun setLoginState(isLogin: Boolean, loginUser: LoginUser?) {
        if (isLogin && loginUser != null) {
            this.viewModel.setLoginState(true, loginUser)
        } else {
            this.viewModel.setLoginState(false, null)
        }
    }

    private fun updateImage(imageUrl: String?) {
        try {
            if (!TextUtils.isEmpty(this.viewModel.imageUrl.value)) {
                this.binding.image.resizeFromDimen(
                        imageUrl,
                        R.dimen.drawer_header_image_width,
                        R.dimen.drawer_header_image_height)
                this.binding.image.visibility = View.VISIBLE
            } else {
                this.binding.image.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {
            this.binding.image.visibility = View.INVISIBLE
        }
    }

    override fun isDisposed(): Boolean {
        return this.subscriptions.isDisposed
    }

    override fun dispose() {
        if (!this.subscriptions.isDisposed)
            this.subscriptions.dispose()
    }
}
