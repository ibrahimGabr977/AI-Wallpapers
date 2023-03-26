package com.hope.igb.aiwallpapers.networking

class WallpaperModelHelper {

    companion object {
        fun modelUrlsToString(wallpaper: WallpaperModel): String {
            return wallpaper.originalUrl + ",,," + wallpaper.croppedUrl
        }


        fun getOriginalUrl(wallpaperUrls: String): String {
            return wallpaperUrls.split(",,,")[0]
        }

        fun getCroppedUrl(wallpaperUrls: String): String {
            return wallpaperUrls.split(",,,")[1]
        }

        fun hasOnlyOneSize(wallpaperUrls: String): Boolean {
            return getCroppedUrl(wallpaperUrls).trim().isEmpty()
        }


        fun modelUsagesToString(wallpaper: WallpaperModel): String {
            return wallpaper.id+",,,"+wallpaper.usageTimes
        }

        fun getId(usagesInString: String) : String{
            return usagesInString.split(",,,")[0]
        }
        
        fun getUsages(usagesInString: String) : Int{
            return usagesInString.split(",,,")[1].toInt()
        }


    }


}