package com.hope.igb.aiwallpapers.screen.display.wallpapersetter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hope.igb.aiwallpapers.R
import com.hope.igb.aiwallpapers.util.ImageLoader
import com.hope.igb.aiwallpapers.networking.WallpaperModelHelper
import com.hope.igb.aiwallpapers.comman.views.BaseObservableViewMvc

@SuppressLint("InflateParams")
class SettingWallpaperWindow(inflater: LayoutInflater, private val targetScreen: Int) :
    BaseObservableViewMvc<SettingWallpaperWindow.Listener>() {


    private var settingWindow: PopupWindow? = null
    private var windowOpening = false



    interface Listener {
        fun onWallpaperSizeSelected(wallpaperUrl: String, targetScreen: Int)
    }

    init {
        setRootView(inflater.inflate(R.layout.setting_wallpaper_window, null, false))
    }


    fun showWindow(wallpaperInString: String) {


        val originalWallpaper: ImageView = findViewById(R.id.settingOriginalWallpaper)
        val croppedWallpaper: ImageView = findViewById(R.id.settingCroppedWallpaper)
        originalWallpaper.clipToOutline = true
        croppedWallpaper.clipToOutline = true

        val selectOriginal: View = findViewById(R.id.selectSetOriginalWallpaper)
        val selectCropped: View = findViewById(R.id.selectSetCroppedWallpaper)


        ImageLoader(getContext()).loadImage(WallpaperModelHelper.getOriginalUrl(wallpaperInString)).into(originalWallpaper)
        ImageLoader(getContext()).loadImage(WallpaperModelHelper.getCroppedUrl(wallpaperInString)).into(croppedWallpaper)



        settingWindow = PopupWindow(
            getRootView(), ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )


        //window shape
        settingWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        settingWindow!!.update(
            0, 0, ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )




        originalWallpaper.setOnClickListener {
            selectCropped.visibility = View.VISIBLE
            val croppedText: TextView = findViewById(R.id.setting_cropped_text)
            croppedText.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_color))

            for (listener in getListeners())
                listener.onWallpaperSizeSelected(WallpaperModelHelper.getOriginalUrl(wallpaperInString), targetScreen)


            dismissWindow()
        }


        croppedWallpaper.setOnClickListener {
            selectOriginal.visibility = View.VISIBLE
            val originalText: TextView = findViewById(R.id.setting_original_text)
            originalText.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_color))

            for (listener in getListeners())
                listener.onWallpaperSizeSelected(WallpaperModelHelper.getCroppedUrl(wallpaperInString), targetScreen)

            dismissWindow()
        }


        //show popup window
        settingWindow!!.showAtLocation(getRootView(), Gravity.CENTER, 0, 0)
        windowOpening = true

    }


     fun dismissWindow() {
            settingWindow!!.dismiss()
            settingWindow = null
         windowOpening = false

    }


    fun isWindowOpening(): Boolean{
        return windowOpening
    }


}