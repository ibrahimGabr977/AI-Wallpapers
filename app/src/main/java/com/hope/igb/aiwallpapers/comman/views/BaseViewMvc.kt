package com.hope.igb.aiwallpapers.comman.views

import android.content.Context
import android.view.View

abstract class BaseViewMvc : ViewMvc {

    private lateinit var rootView : View


   protected fun setRootView(rootView: View){
        this.rootView = rootView
    }


    protected fun <T:View> findViewById(view_id : Int) : T {
        return rootView.findViewById(view_id)
    }

    override fun getRootView(): View {
        return rootView
    }


    protected fun getContext() : Context{
        return rootView.context
    }


}