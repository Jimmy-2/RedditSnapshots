package com.example.snapshotsforreddit.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

//monitor 2 values for switchMap.
//source: https://stackoverflow.com/questions/49493772/mediatorlivedata-or-switchmap-transformation-with-multiple-parameters
class MonitorPair<A, B>(a: LiveData<A>, b: LiveData<B>) : MediatorLiveData<Pair<A?, B?>>() {
    init {
        addSource(a) { value = it to b.value }
        addSource(b) { value = a.value to it }
    }
}


//source: https://medium.com/codex/how-to-use-mediatorlivedata-with-multiple-livedata-types-a40e1a59e8cf
class MonitorTriple<A, B, C>(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>) :
    MediatorLiveData<Triple<A?, B?, C?>>() {
    init {
        addSource(a) { aValue: A -> value = Triple(aValue, b.value, c.value) }
        addSource(b) { bValue: B -> value = Triple(a.value, bValue, c.value) }
        addSource(c) { cValue: C -> value = Triple(a.value, b.value, cValue) }

    }
}