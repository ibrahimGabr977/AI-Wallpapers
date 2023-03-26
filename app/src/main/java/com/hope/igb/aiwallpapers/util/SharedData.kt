package com.hope.igb.aiwallpapers.util

import android.content.Context
import android.content.SharedPreferences

class SharedData(private val context: Context) {



    private val favoriteList = "favoriteList"

    private fun getSharedPreferences(): SharedPreferences? {
        return context.applicationContext.getSharedPreferences("likedWallpapers",0)
    }

    fun addToFavorite(model: String){
        val editor = getSharedPreferences()?.edit()
        val set = getFavoriteList()
        set?.add(model)
        editor?.putStringSet(favoriteList, set)
        editor?.apply()

    }




    fun removeFromFavorite(model: String){
        val editor = getSharedPreferences()?.edit()
        val set = getFavoriteList()
        set?.remove(model)
        editor?.putStringSet(favoriteList, set)
        editor?.apply()
    }

    fun isContains(model: String): Boolean{
        return getFavoriteList()!!.contains(model)
    }


    fun getFavoriteList(): MutableSet<String>? {
        return getSharedPreferences()?.getStringSet(favoriteList, LinkedHashSet<String>())
    }




}