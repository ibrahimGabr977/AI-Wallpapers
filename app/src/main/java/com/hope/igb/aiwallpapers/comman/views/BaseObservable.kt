package com.hope.igb.aiwallpapers.comman.views

import java.util.*
import java.util.concurrent.ConcurrentHashMap

abstract class BaseObservable<LISTENER_CLASS> {

    // thread-safe set of listeners
    private val mListeners: MutableSet<LISTENER_CLASS> = Collections.newSetFromMap(
        ConcurrentHashMap(1)
    )


    fun registerListener(listener: LISTENER_CLASS) {
        mListeners.add(listener)
    }

    fun unregisterListener(listener: LISTENER_CLASS) {
        mListeners.remove(listener)
    }

    protected fun getListeners(): Set<LISTENER_CLASS> {
        return Collections.unmodifiableSet(mListeners)
    }
}