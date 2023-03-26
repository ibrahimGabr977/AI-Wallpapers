package com.hope.igb.aiwallpapers.screen.display.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.hope.igb.aiwallpapers.comman.views.BaseObservable

class PermissionHandler(private val activity: FragmentActivity) :
    BaseObservable<PermissionHandler.PermissionGrantedListener>(),
    ActivityResultCallback<Boolean>{

    interface PermissionGrantedListener {
        fun onPermissionGranted()
        fun onPermissionNotGranted()
    }


    private val launcher =  (activity as ActivityResultCaller)
        .registerForActivityResult(RequestPermission(), this)



    fun isStoragePermissionGranted(): Boolean {
        when {

            (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED &&
                    Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) ->

                //check write external storage permission

                    launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)


            else -> return true
        }

        return false
    }



    override fun onActivityResult(isGranted: Boolean) {
        for (listener in getListeners())
            if (isGranted)
                listener.onPermissionGranted()
            else
                listener.onPermissionNotGranted()
    }

}