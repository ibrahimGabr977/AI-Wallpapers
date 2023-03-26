package com.hope.igb.aiwallpapers.adhelper

import android.content.Context
import com.google.android.gms.ads.MobileAds

class AdmobHelper {


    companion object{
        const val adBannerTop: String = "BANNER_TOP"
        const val adBannerBottom: String = "BANNER_BOTTOM"

        fun initializeAdmob(context: Context){
            MobileAds.initialize(context)
        }
    }



}