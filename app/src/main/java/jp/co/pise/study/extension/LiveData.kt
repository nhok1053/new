package jp.co.pise.study.extension

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations

fun <X> MutableLiveData<X>.default(initialValue: X) = apply { setValue(initialValue) }
fun <X, Y> LiveData<X>.switchMap(func: (X) -> LiveData<Y>) = Transformations.switchMap(this, func)
fun <X, Y> LiveData<X>.map(func: (X) -> Y) = Transformations.map(this, func)

@Suppress("UNCHECKED_CAST")
fun <X: MutableList<Y>, Y> MutableLiveData<X>.add(element: Y) {
    val data = this.value?.apply { add(element) } ?: (mutableListOf<Y>() as X).apply { add(element) }
    this.postValue(data)
}

@Suppress("UNCHECKED_CAST")
fun <X: MutableList<Y>, Y> MutableLiveData<X>.addAll(elements: Collection<Y>) {
    val data = this.value?.apply { addAll(elements) } ?: (mutableListOf<Y>() as X).apply { addAll(elements) }
    this.postValue(data)
}