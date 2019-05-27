package jp.co.pise.studyapp.extension

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding

val Boolean?.unwrap get() = this ?: false
val Int?.unwrap get() = this ?: 0
val String?.unwrap get() = this ?: ""

fun <V: ViewDataBinding> V.owner(owner: LifecycleOwner): V {
    this.lifecycleOwner = owner
    return this
}