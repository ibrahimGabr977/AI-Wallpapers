package com.hope.igb.aiwallpapers.screen.display

import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.hope.igb.aiwallpapers.R
import com.hope.igb.aiwallpapers.adhelper.AdBannerHelper
import com.hope.igb.aiwallpapers.adhelper.AdInterstitialHelper
import com.hope.igb.aiwallpapers.adhelper.AdmobHelper
import com.hope.igb.aiwallpapers.comman.BaseFragment
import com.hope.igb.aiwallpapers.comman.messagehelper.SystemMessageHelper
import com.hope.igb.aiwallpapers.networking.WallpaperModelHelper
import com.hope.igb.aiwallpapers.networking.firebasedatabase.EditWallpaperDetailsUseCase
import com.hope.igb.aiwallpapers.networking.firebasestorage.WallpaperDownloadUseCase
import com.hope.igb.aiwallpapers.screen.display.progresswindow.ProcessProgressWindow
import com.hope.igb.aiwallpapers.screen.display.util.PermissionHandler
import com.hope.igb.aiwallpapers.screen.display.wallpapersetter.WallpaperSetter
import com.hope.igb.aiwallpapers.util.Constants
import com.hope.igb.aiwallpapers.util.SharedData


class DisplayFragment : BaseFragment(), DisplayViewMvc.DisplayListener,
    PermissionHandler.PermissionGrantedListener, ProcessProgressWindow.Listener,
    WallpaperDownloadUseCase.DownloadListener, AdBannerHelper.AdmobBannerListener {

    private lateinit var viewMvc: DisplayViewMvc
    private lateinit var sharedData: SharedData
    private lateinit var wallpaperInString: String
    private lateinit var systemMessageHelper: SystemMessageHelper

    private lateinit var adBannerTop: AdBannerHelper
    private lateinit var adBannerBottom: AdBannerHelper
    private lateinit var adInterstitial: AdInterstitialHelper





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedData = SharedData(requireContext())
        wallpaperInString = DisplayFragmentArgs.fromBundle(requireArguments()).wallpaperUrls

        viewMvc = DisplayViewMvc(inflater, container, wallpaperInString, sharedData)

        systemMessageHelper = SystemMessageHelper(requireContext())




        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewMvc.isAnyWindowOpening())
                        viewMvc.closeAnyOpeningWindow()
                    else {
                        isEnabled = false
                        onBackClicked()
                    }
                }
            }
        )



        adBannerTop = AdBannerHelper(AdmobHelper.adBannerTop, viewMvc.adViewTop)
        adBannerBottom = AdBannerHelper(AdmobHelper.adBannerBottom, viewMvc.adViewBottom)
        adInterstitial = AdInterstitialHelper(requireActivity())




        return viewMvc.getRootView()
    }


    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)

        adBannerTop.loadAd()
        adBannerBottom.loadAd()
        adInterstitial.initAd(getString(R.string.interstitial_ad_id1))
        adBannerTop.registerListener(this)
        adBannerBottom.registerListener(this)
    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)

        adBannerTop.unregisterListener(this)
        adBannerBottom.unregisterListener(this)
    }


    override fun onDownloadItemSelected() {
        grantPermissionAndDownload()
    }

    private fun grantPermissionAndDownload() {

        if (getPermissionHandler().isStoragePermissionGranted()) {
            viewMvc.openDownloadingWindowAndStart(this)

        } else
            getPermissionHandler().registerListener(this)


    }

    override fun startDownloading(wallpaperUrl: String) {
        val downloadUseCase = WallpaperDownloadUseCase(wallpaperUrl)
        downloadUseCase.registerListener(this)
        downloadUseCase.downloadWallpaper()

    }


    override fun onProgressChanged(progress: Int) {
        viewMvc.setProcessProgress(progress)
    }

    override fun onDownloaded(filePath: String) {
        viewMvc.onProcessFinished()
        systemMessageHelper.showShortMessage("Wallpaper successfully downloaded to $filePath.")
        boostWallpaperUsages()
        adInterstitial.showAd()
        refreshGalleryToShowTheSavedImage(filePath)
    }

    private fun refreshGalleryToShowTheSavedImage(filePath: String) {
        MediaScannerConnection.scanFile(
            requireContext(), arrayOf(filePath), null,
            null
        )
    }


    override fun onFailedDownloaded(error_message: String?) {
        viewMvc.onProcessFinished()
        systemMessageHelper.showLongMessage(error_message!!)
    }


    override fun onPermissionGranted() {
        getPermissionHandler().unregisterListener(this)
        grantPermissionAndDownload()
    }

    override fun onPermissionNotGranted() {
        getPermissionHandler().unregisterListener(this)
        systemMessageHelper.showLongMessage("Please give a storage permission to share or save an image")
    }


    override fun onSetWallpaperItemSelected(wallpaperUrl: String, targetScreen: Int) {
        val wallpaperSetter = WallpaperSetter(requireActivity(), wallpaperUrl)
        when (targetScreen) {
            Constants.Integers.HOMESCREEN -> {
                wallpaperSetter.setWallpaper()
                adInterstitial.showAd()
            }


            Constants.Integers.LOCKSCREEN -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperSetter.setLockScreen()
                    adInterstitial.showAd()
                } else
                    systemMessageHelper.showShortMessage("Setting lock screen wallpaper need android 7 or higher.")


            }else -> {
            wallpaperSetter.setBoth()
        }


        }
        boostWallpaperUsages()
    }


    private fun boostWallpaperUsages() {
        val wallpaperUsages = DisplayFragmentArgs.fromBundle(requireArguments()).wallpaperUsages
        if (wallpaperUsages.trim().isNotEmpty()) {
            val editUseCase =
                EditWallpaperDetailsUseCase(WallpaperModelHelper.getId(wallpaperUsages))
            editUseCase.editUsages(WallpaperModelHelper.getUsages(wallpaperUsages) + 1)
        }
    }


    override fun onBackClicked() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    override fun onAdLoadedSuccess(adName: String) {
        if (adName == AdmobHelper.adBannerBottom)
            viewMvc.adViewBottom.visibility = View.VISIBLE
        else
            viewMvc.adViewTop.visibility = View.VISIBLE
    }

    override fun onAdLoadedFailed(adName: String) {
        if (adName == AdmobHelper.adBannerBottom)
            viewMvc.adViewBottom.visibility = View.INVISIBLE
        else
            viewMvc.adViewTop.visibility = View.INVISIBLE
    }

}


