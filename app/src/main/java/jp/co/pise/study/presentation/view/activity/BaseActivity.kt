package jp.co.pise.study.presentation.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.co.pise.study.presentation.lifecycle.DisposableObserver

abstract class BaseActivity : AppCompatActivity() {
    protected val disableObserver = DisposableObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lifecycle.addObserver(this.disableObserver)
    }
}