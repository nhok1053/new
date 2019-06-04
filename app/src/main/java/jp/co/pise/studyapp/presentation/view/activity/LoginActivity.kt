package jp.co.pise.studyapp.presentation.view.activity

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import jp.co.pise.studyapp.R
import jp.co.pise.studyapp.databinding.ActivityLoginBinding
import jp.co.pise.studyapp.definition.Message
import jp.co.pise.studyapp.extension.addBug
import jp.co.pise.studyapp.extension.owner
import jp.co.pise.studyapp.presentation.StudyApp
import jp.co.pise.studyapp.presentation.viewmodel.activity.LoginActivityViewModel
import javax.inject.Inject

class LoginActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: LoginActivityViewModel
    lateinit var binding: ActivityLoginBinding

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        // setting binding
        this.binding = DataBindingUtil
                .setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
                .owner(this)
        this.binding.viewModel = viewModel
        this.viewModel.addBug(this.subscriptions)

        // setting toolbar
        (this.binding.toolbar as Toolbar?)?.let {
            setSupportActionBar(it)
        }

        subscribe()
    }

    public override fun onResume() {
        super.onResume()
    }

    public override fun onPause() {
        super.onPause()
    }

    private fun subscribe() {
        this.viewModel.onLogin.observeOn(AndroidSchedulers.mainThread()).subscribe({ result ->
            StudyApp.instance.login(result)
            Toast.makeText(this, Message.LOGIN, Toast.LENGTH_SHORT).show()
            finish()
        }, { }).addBug(this.subscriptions)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val manager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
        return super.onTouchEvent(event)
    }
}
