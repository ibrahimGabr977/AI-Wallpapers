package com.hope.igb.aiwallpapers.comman

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.hope.igb.aiwallpapers.screen.BaseActivity
import com.hope.igb.aiwallpapers.comman.screennavigator.ScreenNavigator
import com.hope.igb.aiwallpapers.screen.display.util.PermissionHandler

open class BaseFragment : Fragment() {

    private var screenNavigator: ScreenNavigator? = null


    private fun getNavHostFragment(): NavHostFragment {
        return (requireActivity() as BaseActivity).getNavHostFragment()!!
    }

     fun getPermissionHandler(): PermissionHandler {
        return (requireActivity() as BaseActivity).getPermissionHandler()

    }

    fun getScreenNavigator(): ScreenNavigator? {
        if (screenNavigator == null)
            screenNavigator = ScreenNavigator(getNavHostFragment())

        return screenNavigator
    }


}