package jp.co.pise.study.presentation.view.activity

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import dagger.android.AndroidInjection
import jp.co.pise.study.R
import jp.co.pise.study.databinding.ActivityMainBinding
import jp.co.pise.study.domain.model.LoginChallenge
import jp.co.pise.study.domain.model.LoginResult
import jp.co.pise.study.extension.addBug
import jp.co.pise.study.presentation.viewmodel.activity.MainActivityViewModel
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: MainActivityViewModel
    lateinit var binding: ActivityMainBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        this.binding.setLifecycleOwner(this)
        this.binding.viewModel = this.viewModel

        this.viewModel.loginChallenge.observe(this, Observer<LoginChallenge> {
            this.binding.isLoading = true
        })
        this.viewModel.loginResult.observe(this, Observer<LoginResult> {
            this.binding.isLoading = false
        })
        this.viewModel.skipCommand.subscribe {
            val intent = Intent(this, SecondActivity::class.java)
            this.startActivity(intent)
        }
        this.viewModel.addBug(this.disableObserver.subscriptions)
    }
}
