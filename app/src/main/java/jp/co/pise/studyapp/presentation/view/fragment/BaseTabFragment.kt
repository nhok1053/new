package jp.co.pise.studyapp.presentation.view.fragment

import android.support.annotation.IdRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import jp.co.pise.studyapp.R

abstract class BaseTabFragment : BaseFragment() {
    @IdRes
    protected var containerId = R.id.container

    fun stackChildFragment(fragment: Fragment, tag: String, isSingleton: Boolean = true) {
        childFragmentManager.executePendingTransactions()
        if (isSingleton)
            childFragmentManager.findFragmentByTag(tag)?.let { childFragmentManager.beginTransaction().remove(it).commitNow() }

        childFragmentManager.beginTransaction().apply {
            addToBackStack(null)
            add(containerId, fragment, tag)
            commit()
        }
    }

    fun dismissDialogFragment() {
        childFragmentManager.executePendingTransactions()
        childFragmentManager.fragments
                .filter { it is DialogFragment }
                .map { it as DialogFragment }
                .forEach(DialogFragment::dismiss)
    }

    fun popBackStack(): Boolean {
        var isDoBack = false
        childFragmentManager.executePendingTransactions()
        if (childFragmentManager.fragments.size > 1) {
            // 最後のFragmentじゃなければ、popBackStackを実行
            childFragmentManager.popBackStackImmediate()
            isDoBack = true
        }
        return isDoBack
    }
}
