package jp.co.pise.studyapp.extension

import android.arch.lifecycle.*

fun <X> MutableLiveData<X>.default(initialValue: X) = apply { setValue(initialValue) }
fun <X, Y> LiveData<X>.switchMap(func: (X) -> LiveData<Y>) = Transformations.switchMap(this, func)
fun <X, Y> LiveData<X>.map(func: (X) -> Y) = Transformations.map(this, func)
fun <X: MutableList<Y>, Y> MutableLiveData<X>.add(element: Y) = this.value?.apply { add(element) }?.let { postValue(it) }
fun <X: MutableList<Y>, Y> MutableLiveData<X>.addAll(elements: Collection<Y>) = this.value?.apply { addAll(elements) }?.let { postValue(it) }

fun <T> LiveData<T>.replaceObserve(owner: LifecycleOwner, observer: Observer<T>) {
    this.removeObservers(owner)
    this.observe(owner, observer)
}