package com.hope.igb.aiwallpapers.adhelper

import android.annotation.SuppressLint
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.hope.igb.aiwallpapers.comman.views.BaseObservable

@SuppressLint("SuspiciousIndentation")
class AdBannerHelper(private val adName: String, private val adView: AdView) :
    BaseObservable<AdBannerHelper.AdmobBannerListener>() {


    interface AdmobBannerListener {

         fun onAdLoadedSuccess(adName: String)
         fun onAdLoadedFailed(adName: String)
    }

    private val adRequest: AdRequest = AdRequest.Builder().build()


    fun loadAd() {
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {


            override fun onAdLoaded() {
                for (listener in getListeners())
                listener.onAdLoadedSuccess(adName)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                for (listener in getListeners())
                listener.onAdLoadedFailed(adName)
            }

        }
    }


}