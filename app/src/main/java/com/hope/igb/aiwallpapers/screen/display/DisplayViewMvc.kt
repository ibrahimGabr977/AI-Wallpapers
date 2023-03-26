package com.hope.igb.aiwallpapers.screen.display


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdView
import com.hope.igb.aiwallpapers.R
import com.hope.igb.aiwallpapers.comman.messagehelper.SystemMessageHelper
import com.hope.igb.aiwallpapers.comman.views.BaseObservableViewMvc
import com.hope.igb.aiwallpapers.networking.WallpaperModelHelper
import com.hope.igb.aiwallpapers.screen.display.menuwindow.WallpaperOptionsWindow
import com.hope.igb.aiwallpapers.screen.display.progresswindow.ProcessProgressWindow
import com.hope.igb.aiwallpapers.screen.display.wallpapersetter.SettingWallpaperWindow
import com.hope.igb.aiwallpapers.util.Constants.Integers
import com.hope.igb.aiwallpapers.util.ImageLoader
import com.hope.igb.aiwallpapers.util.SharedData
import com.ortiz.touchview.TouchImageView


@SuppressLint("ClickableViewAccessibility")
class DisplayViewMvc(
    private val inflater: LayoutInflater,
    parent: ViewGroup?,
    private val wallpaperInString: String,
    private val sharedData: SharedData
) :
    BaseObservableViewMvc<DisplayViewMvc.DisplayListener>(), WallpaperOptionsWindow.Listener,
    SettingWallpaperWindow.Listener {
    interface DisplayListener {
        fun onBackClicked()
        fun onSetWallpaperItemSelected(wallpaperUrl: String, targetScreen: Int)
        fun onDownloadItemSelected()
    }


    private val addToFavorite: ImageView
    private val imageLoader: ImageLoader
    private val displayImage: TouchImageView
    private var progressWindow: ProcessProgressWindow? = null
    private var wallpaperOptionsWindow: WallpaperOptionsWindow? = null
    private var settingWallpaperWindow: SettingWallpaperWindow? = null
    private val systemMessageHelper: SystemMessageHelper

    val adViewTop: AdView
    val adViewBottom: AdView


    init {
        setRootView(inflater.inflate(R.layout.display_fragment, parent, false))
        imageLoader = ImageLoader(getContext())
        displayImage = findViewById(R.id.display_wallpaper)

        imageLoader.loadImage(WallpaperModelHelper.getOriginalUrl(wallpaperInString))
            .into(displayImage)




        addToFavorite = findViewById(R.id.add_to_favorite)
        initButtons()


        systemMessageHelper = SystemMessageHelper(getContext())

        adViewTop = findViewById(R.id.displayAdViewTop)
        adViewBottom = findViewById(R.id.displayAdViewBottom)


    }


    private fun initButtons() {
        findViewById<ImageView>(R.id.display_back).setOnClickListener {
            for (listener in getListeners())
                listener.onBackClicked()
        }


        findViewById<ImageView>(R.id.options_menu).setOnClickListener {
            onOptionsMenuClicked()
        }

        if (isWallpaperAlreadyAddedToFavorite())
            addToFavorite.setImageResource(R.drawable.favorite_selected)


        addToFavorite.setOnClickListener { onAddToFavoriteClicked() }


    }


    private fun onAddToFavoriteClicked() {
        if (!isWallpaperAlreadyAddedToFavorite())
            addWallpaperToFavorite()
        else
            removeWallpaperFromFavorite()
    }


    private fun addWallpaperToFavorite() {
        addToFavorite.setImageResource(R.drawable.favorite_selected)
        systemMessageHelper.showShortMessage("Wallpaper added to favorite.")
        sharedData.addToFavorite(wallpaperInString)

    }

    private fun removeWallpaperFromFavorite() {
        addToFavorite.setImageResource(R.drawable.favorite_unselected)
        systemMessageHelper.showShortMessage("Wallpaper removed from favorite.")
        sharedData.removeFromFavorite(wallpaperInString)

    }


    private fun isWallpaperAlreadyAddedToFavorite(): Boolean {
        return sharedData.isContains(wallpaperInString)
    }


    private fun onOptionsMenuClicked() {
        wallpaperOptionsWindow = WallpaperOptionsWindow(inflater)
        wallpaperOptionsWindow?.showWallpaperOptions()
        wallpaperOptionsWindow?.registerListener(this)
    }

    override fun onMenuOptionsClicked(id: Int) {
        if (isWallpaperAvailableForDownloading()) {
            when (id) {
                R.id.download -> onDownloadItemSelected()
                R.id.set_wallpaper -> onSetWallpaperItemSelected(Integers.HOMESCREEN)
                R.id.set_lockscreen -> onSetWallpaperItemSelected(Integers.LOCKSCREEN)
                else -> onSetWallpaperItemSelected(Integers.BOTHSCREENS)
            }
        } else
            systemMessageHelper.showLongMessage("please check your internet connection and try again.")
    }


    private fun onDownloadItemSelected() {
        for (listener in getListeners())
            listener.onDownloadItemSelected()
    }

    fun openDownloadingWindowAndStart(listener: ProcessProgressWindow.Listener) {

        progressWindow = ProcessProgressWindow(inflater, wallpaperInString)
        progressWindow?.registerListener(listener)
        progressWindow?.showWindow()

    }

    fun setProcessProgress(progress: Int) {
        progressWindow?.setProgress(progress)
    }

    fun onProcessFinished() {
        progressWindow?.onDownloadingFinished()
        progressWindow = null
    }


    private fun onSetWallpaperItemSelected(targetScreen: Int) {

        if (WallpaperModelHelper.hasOnlyOneSize(wallpaperInString))
            for (listener in getListeners())
                listener.onSetWallpaperItemSelected(
                    WallpaperModelHelper.getOriginalUrl(wallpaperInString),
                    targetScreen
                )
        else {
            settingWallpaperWindow = SettingWallpaperWindow(inflater, targetScreen)
            settingWallpaperWindow?.showWindow(wallpaperInString)
            settingWallpaperWindow?.registerListener(this)
        }

    }


    override fun onWallpaperSizeSelected(wallpaperUrl: String, targetScreen: Int) {
        for (listener in getListeners())
            listener.onSetWallpaperItemSelected(wallpaperUrl, targetScreen)
    }

    override fun unregisterListener(listener: DisplayListener) {
        super.unregisterListener(listener)
        if (isAnyWindowOpening())
            closeAnyOpeningWindow()
    }

    fun closeAnyOpeningWindow() {

        wallpaperOptionsWindow?.dismissWindow()
        wallpaperOptionsWindow = null

        progressWindow?.dismissWindow()
        progressWindow = null


        settingWallpaperWindow?.dismissWindow()
        settingWallpaperWindow = null


    }


    fun isAnyWindowOpening(): Boolean {
        return (wallpaperOptionsWindow != null && wallpaperOptionsWindow?.isWindowOpening()!!) ||
                (progressWindow != null && progressWindow?.isWindowOpening()!!) ||
                (settingWallpaperWindow != null && settingWallpaperWindow?.isWindowOpening()!!)
    }

    private fun isWallpaperAvailableForDownloading(): Boolean {
        return displayImage.drawable != ContextCompat.getDrawable(
            getContext(),
            R.drawable.wallpaper_error
        ) &&
                !displayImage.drawable.toString().contains("CircularProgressDrawable")
    }


}