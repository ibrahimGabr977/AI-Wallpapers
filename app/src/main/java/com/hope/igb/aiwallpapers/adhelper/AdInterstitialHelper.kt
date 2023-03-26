package com.hope.igb.aiwallpapers.adhelper

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.hope.igb.aiwallpapers.util.MuteSounds


class AdInterstitialHelper(private val activity: Activity) {




    private var mInterstitialAd: InterstitialAd? = null


    fun initAd(adUnitId: String){
        InterstitialAd.load(activity, adUnitId, AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback(){

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                mInterstitialAd = null

            }

        })
    }

    fun showAd(){
        if (mInterstitialAd != null)
            mInterstitialAd?.show(activity)
        MuteSounds.muteAds(activity)
    }






}