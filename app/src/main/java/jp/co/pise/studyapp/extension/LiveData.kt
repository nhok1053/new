package jp.co.pise.studyapp.extension

import android.arch.lifecycle.*

fun <X> MutableLiveData<X>.default(initialValue: X) = apply { setValue(initialValue) }
fun <X, Y> LiveData<X>.switchMap(func: (X) -> LiveData<Y>) = Transformations.switchMap(this, func)
fun <X, Y> LiveData<X>.map(func: (X) -> Y) = Transformations.map(this, func)
fun <X> MutableLiveData<MutableList<X>>.add(element: X) = postValue((this.value?.apply { add(element) } ?: mutableListOf()))
fun <X> MutableLiveData<MutableList<X>>.addAll(elements: Collection<X>) = postValue((this.value?.apply { addAll(elements) } ?: mutableListOf()))
fun <X> MutableLiveData<MutableList<X>>.clear() = postValue(mutableListOf())

fun <T> LiveData<T>.replaceObserve(owner: LifecycleOwner, observer: Observer<T>) {
    this.removeObservers(owner)
    this.observe(owner, observer)
}