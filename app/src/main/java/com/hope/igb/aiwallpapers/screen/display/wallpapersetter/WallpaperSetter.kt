package com.hope.igb.aiwallpapers.screen.display.wallpapersetter

import android.app.Activity
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import com.hope.igb.aiwallpapers.comman.messagehelper.SystemMessageHelper
import java.net.URL


class WallpaperSetter(private val activity: Activity, private val wallpaperInString: String) {


    private val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(activity)
    private val systemMessageHelper = SystemMessageHelper(activity)
    private var thread: Thread? = null


    fun setWallpaper() {

        thread = Thread {
            val wallpaper = getBitmapFromURL(wallpaperInString)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                wallpaperManager.setBitmap(wallpaper, null, true, WallpaperManager.FLAG_SYSTEM)
            else
                wallpaperManager.setBitmap(wallpaper)

        }
        thread?.start()
        successMessage("Home screen wallpaper set successfully.")
        thread = null

    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun setLockScreen() {
        thread = Thread {
            val wallpaper = getBitmapFromURL(wallpaperInString)
            wallpaperManager.setBitmap(wallpaper, null, true, WallpaperManager.FLAG_LOCK)
        }
        thread?.start()
        successMessage("Lock screen wallpaper set successfully.")
        thread = null

    }




    fun setBoth() {

       thread = Thread{
           val wallpaper = getBitmapFromURL(wallpaperInString)
           wallpaperManager.setBitmap(wallpaper)

           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               wallpaperManager.setBitmap(wallpaper, null, true, WallpaperManager.FLAG_LOCK)
           }
        }

        thread?.start()
        successMessage("Wallpaper set successfully.")
        thread = null
    }




    private fun getBitmapFromURL(src: String?): Bitmap? {
        return BitmapFactory.decodeStream(URL(src).openStream())

    }



    private fun successMessage(message: String) {
        activity.runOnUiThread {
            systemMessageHelper.showShortMessage(message)
        }
    }

}