package jp.co.pise.studyapp.presentation.view.fragment

import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import jp.co.pise.studyapp.R

abstract class BaseTabFragment : BaseFragment() {
    fun stackChildFragment(fragment: Fragment, tag: String, isRemove: Boolean = true) {
        val transaction = childFragmentManager.beginTransaction()

        if (isRemove)
            childFragmentManager.findFragmentByTag(tag)?.let { transaction.remove(it) }

        transaction.addToBackStack(null)
        transaction.add(R.id.container, fragment, tag)
        transaction.commit()
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
        if (childFragmentManager.fragments.size > 1) {
            // 最後のFragmentじゃなければ、popBackStackを実行
            childFragmentManager.popBackStackImmediate()
            isDoBack = true
        }
        return isDoBack
    }
}
