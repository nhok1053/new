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
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.DrawerHeaderBinding
import jp.co.pise.studyapp.domain.model.LoginUser
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.extension.resizeFromDimen
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.viewmodel.customview.DrawerHeaderViewModel
import javax.inject.Inject

@SuppressLint("ViewConstructor")
class DrawerHeaderView(context: Context, owner: LifecycleOwner) : FrameLayout(context) {
    @Inject
    internal lateinit var viewModel: DrawerHeaderViewModel
    private val binding: DrawerHeaderBinding

    init {
        StudyApp.instance.appComponent.inject(this)

        this.binding = DataBindingUtil
                .inflate<DrawerHeaderBinding>(LayoutInflater.from(context), R.layout.drawer_header, this, true)
                .owner(owner)
        this.binding.viewModel = this.viewModel

        this.viewModel.imageUrl.observe(owner, Observer { updateImage(it) })
    }

    fun setLogin(isLogin: Boolean, loginUser: LoginUser) {
        this.viewModel.setLogin(isLogin, loginUser)
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
}
