package com.hope.igb.aiwallpapers.comman.views

import java.util.Collections
import java.util.concurrent.ConcurrentHashMap


abstract class BaseObservableViewMvc<LISTENER_TYPE> : BaseViewMvc(),
    ObservableViewMvc<LISTENER_TYPE> {


    private val listeners: MutableSet<LISTENER_TYPE> = Collections.newSetFromMap(
         ConcurrentHashMap(1)
    )



    override fun registerListener(listener: LISTENER_TYPE) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: LISTENER_TYPE) {
        listeners.remove(listener)
    }


    protected fun getListeners() : Set<LISTENER_TYPE>{
        return Collections.unmodifiableSet(listeners)
    }

}