package cz.marvincz.rssnotifier.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Sets the value to the result of a function that is called when both `LiveData`s have data
 * or when they receive updates after that.
 */
fun <A, B, T> LiveData<A>.combine(other: LiveData<B>, onChange: (A, B) -> T): MediatorLiveData<T> {
    var thisEmitted = false
    var otherEmitted = false

    val result = MediatorLiveData<T>()

    val merge = {
        if (thisEmitted && otherEmitted) {
            result.value = onChange.invoke(value!!, other.value!!)
        }
    }

    result.addSource(this) { thisEmitted = true; merge.invoke() }
    result.addSource(other) { otherEmitted = true; merge.invoke() }

    return result
}