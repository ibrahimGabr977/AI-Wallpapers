package com.hope.igb.aiwallpapers.screen.display.progresswindow

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hope.igb.aiwallpapers.R
import com.hope.igb.aiwallpapers.util.ImageLoader
import com.hope.igb.aiwallpapers.networking.WallpaperModelHelper
import com.hope.igb.aiwallpapers.comman.views.BaseObservableViewMvc

@SuppressLint("InflateParams")
class ProcessProgressWindow(inflater: LayoutInflater, private val wallpaperInString: String) :
    BaseObservableViewMvc<ProcessProgressWindow.Listener>() {


    private var progressWindow: PopupWindow? = null
    private lateinit var progressValueView: TextView
    private lateinit var process: TextView
    private lateinit var progressBar: ProgressBar
    private var windowOpening = true


    interface Listener {
        fun startDownloading(wallpaperUrl: String)

    }


    init {
        setRootView(inflater.inflate(R.layout.process_progress_window, null, false))
    }


    //for setting up create post popup window
    @SuppressLint("ClickableViewAccessibility")
    fun showWindow() {


        val originalWallpaper: ImageView = findViewById(R.id.originalWallpaper)
        val croppedWallpaper: ImageView = findViewById(R.id.croppedWallpaper)
        originalWallpaper.clipToOutline = true
        croppedWallpaper.clipToOutline = true

        val croppedText: TextView = findViewById(R.id.process_cropped_text)



        progressValueView = findViewById(R.id.progress)
        process = findViewById(R.id.process)
        progressBar = findViewById(R.id.progressBar)

        val cancel: TextView = findViewById(R.id.close_process_window)



        ImageLoader(getContext()).loadImage(WallpaperModelHelper.getOriginalUrl(wallpaperInString))
            .into(originalWallpaper)

        if (WallpaperModelHelper.hasOnlyOneSize(wallpaperInString)) {
            croppedWallpaper.visibility = View.GONE
            croppedText.visibility = View.GONE
            startDownloading(WallpaperModelHelper.getOriginalUrl(wallpaperInString))

        } else
            ImageLoader(getContext()).loadImage(WallpaperModelHelper.getCroppedUrl(wallpaperInString))
                .into(croppedWallpaper)




        progressWindow = PopupWindow(
            getRootView(), ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        progressWindow!!.isOutsideTouchable = false
        progressWindow!!.isFocusable = false


        progressWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressWindow!!.update(
            0, 0, ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )




        if (!WallpaperModelHelper.hasOnlyOneSize(wallpaperInString)) {
            originalWallpaper.setOnClickListener {

                val croppedViewHider: View = findViewById(R.id.selectCroppedWallpaper)
                croppedViewHider.visibility = View.VISIBLE

                croppedText.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_color))

                startDownloading(WallpaperModelHelper.getOriginalUrl(wallpaperInString))

            }

            croppedWallpaper.setOnClickListener {
                val originalViewHider: View = findViewById(R.id.selectOriginalWallpaper)
                originalViewHider.visibility = View.VISIBLE

                val originalText: TextView = findViewById(R.id.process_original_text)
                originalText.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_color))


                startDownloading(WallpaperModelHelper.getCroppedUrl(wallpaperInString))

            }

        }


        cancel.setOnClickListener {
            dismissWindow()
        }


        progressWindow!!.showAtLocation(getRootView(), Gravity.CENTER, 0, 0)
        windowOpening = true
    }


    fun dismissWindow() {
        progressWindow?.dismiss()
        progressWindow = null
        windowOpening = false
    }


    private fun startDownloading(wallpaperUrl: String) {
        process.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        progressValueView.text = "0%"

        for (listener in getListeners())
            listener.startDownloading(wallpaperUrl)
    }


    fun setProgress(progress: Int) {
        progressBar.progress = progress
    }


    fun onDownloadingFinished() {
        dismissWindow()
    }

    fun isWindowOpening(): Boolean {
        return windowOpening
    }

}