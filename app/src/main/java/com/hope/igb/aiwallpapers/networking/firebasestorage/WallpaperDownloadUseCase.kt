package com.hope.igb.aiwallpapers.networking.firebasestorage

import android.os.Build
import android.os.Environment
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hope.igb.aiwallpapers.comman.views.BaseObservable
import java.io.File

class WallpaperDownloadUseCase(downloadUrl: String):
    BaseObservable<WallpaperDownloadUseCase.DownloadListener>() {


    interface DownloadListener {
        fun onDownloaded(filePath: String)
        fun onFailedDownloaded(error_message: String?)
        fun onProgressChanged(progress: Int)
    }


    private val storageReference: StorageReference

    private val appFolder : File


    init {
        appFolder = baseFolder()
       storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(downloadUrl)
    }



    private fun baseFolder() : File{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AiWallpapers")
        else
            File(Environment.getExternalStorageDirectory(), "AiWallpapers")
    }






    fun downloadWallpaper() {
        if(!appFolder.exists())
            appFolder.mkdir()

        val wallpaperFile = File(baseFolder(), "AIWallpaper_${System.currentTimeMillis()}.jpg")

        storageReference.getFile(wallpaperFile)
            .addOnSuccessListener {

                //downloaded succeed
                for (listener in getListeners()) listener.onDownloaded(wallpaperFile.path)
            }


            .addOnFailureListener { e: Exception ->

                //downloaded failed
                for (listener in getListeners()) listener.onFailedDownloaded(e.message)
            }


            .addOnProgressListener { snapshot: FileDownloadTask.TaskSnapshot ->

                //calculating progress percentage
                val progress =
                    100.0 * snapshot.bytesTransferred / snapshot.totalByteCount

                //listen for downloading progress
                for (listener in getListeners()) listener.onProgressChanged(progress.toInt())
            }
    }


}