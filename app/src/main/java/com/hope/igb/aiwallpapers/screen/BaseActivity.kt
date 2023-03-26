package com.hope.igb.aiwallpapers.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.hope.igb.aiwallpapers.R
import com.hope.igb.aiwallpapers.adhelper.AdmobHelper
import com.hope.igb.aiwallpapers.screen.display.util.PermissionHandler

class BaseActivity : AppCompatActivity() {
    private lateinit var permissionHandler: PermissionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        permissionHandler  = PermissionHandler(this)


        if (savedInstanceState == null)
            AdmobHelper.initializeAdmob(this)


    }

    fun getNavHostFragment(): NavHostFragment? {
        return supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment?
    }

    fun getPermissionHandler(): PermissionHandler {
        return permissionHandler
    }


}