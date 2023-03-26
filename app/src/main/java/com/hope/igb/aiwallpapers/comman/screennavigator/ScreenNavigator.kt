package com.hope.igb.aiwallpapers.comman.screennavigator

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.hope.igb.aiwallpapers.screen.favorite.FavoriteFragmentDirections
import com.hope.igb.aiwallpapers.screen.main.MainFragmentDirections

class ScreenNavigator(navHostFragment: NavHostFragment){
    private val navController: NavController = navHostFragment.navController

    fun toFavoriteList(){
        navController.navigate(MainFragmentDirections.actionMainFragmentToFavoriteFragment())
    }

    fun toDisplayFragmentFromFavorite(wallpaperUrls: String){


        val action = FavoriteFragmentDirections
            .actionFavoriteFragmentToDisplayFragment()
            .setWallpaperUrls(wallpaperUrls)

        navController.navigate(action)
    }


    fun toDisplayFragmentFromMain(wallpaperUrls: String, wallpaperUsages: String){


        val action = MainFragmentDirections
            .actionMainFragmentToDisplayFragment()
            .setWallpaperUrls(wallpaperUrls)
            .setWallpaperUsages(wallpaperUsages)


        navController.navigate(action)
    }
}