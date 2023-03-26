package com.hope.igb.aiwallpapers.screen.display.menuwindow

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.view.View.OnClickListener
import android.widget.PopupWindow
import android.widget.TextView
import com.hope.igb.aiwallpapers.R
import com.hope.igb.aiwallpapers.comman.views.BaseObservableViewMvc

@SuppressLint("InflateParams")
class WallpaperOptionsWindow(inflater: LayoutInflater) :
    BaseObservableViewMvc<WallpaperOptionsWindow.Listener>(), OnClickListener {


    private var optionsWindow: PopupWindow? = null
    private var windowOpening = false

    interface Listener {
        fun onMenuOptionsClicked(id: Int)

    }


    init {
        setRootView(inflater.inflate(R.layout.wallpaper_options_window, null, false))
    }




    //for setting up create post popup window
    fun showWallpaperOptions() {

        findViewById<TextView>(R.id.download).setOnClickListener(this)
        findViewById<TextView>(R.id.set_wallpaper).setOnClickListener(this)
        findViewById<TextView>(R.id.set_lockscreen).setOnClickListener(this)
        findViewById<TextView>(R.id.set_both).setOnClickListener(this)




        val closeMenu = findViewById<TextView>(R.id.close_menu)

        optionsWindow = PopupWindow(
            getRootView(), ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        //show and hide from bottom animation
        optionsWindow!!.animationStyle = R.style.Animation

        //window shape
        optionsWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        optionsWindow!!.update(
            0, 0, ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )


        //close create post window
        closeMenu.setOnClickListener {
            dismissWindow()
        }





        //show popup window
        optionsWindow!!.showAtLocation(getRootView(), Gravity.CENTER, 0, 0)
      windowOpening = true
    }


    fun dismissWindow() {
        optionsWindow?.dismiss()
        optionsWindow = null
        windowOpening = false

    }



    override fun onClick(p0: View?) {
        for (listener in getListeners())
            listener.onMenuOptionsClicked(p0!!.id)

        dismissWindow()
    }

    fun isWindowOpening(): Boolean{
        return windowOpening
    }


}