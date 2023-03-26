package com.hope.igb.aiwallpapers.networking

data class WallpaperModel(val id: String,
                          val category: String,
                          val originalUrl: String,
                          val croppedUrl: String,
                          val usageTimes: Int){

    constructor(): this("", "", "","", 0)
}