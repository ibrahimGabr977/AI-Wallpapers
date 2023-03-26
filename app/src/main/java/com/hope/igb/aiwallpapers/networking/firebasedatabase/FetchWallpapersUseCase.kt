package com.hope.igb.aiwallpapers.networking.firebasedatabase

import com.google.firebase.database.*
import com.hope.igb.aiwallpapers.networking.WallpaperModel
import com.hope.igb.aiwallpapers.comman.views.BaseObservable

class FetchWallpapersUseCase : BaseObservable<FetchWallpapersUseCase.Listener>() {
    interface Listener {
        fun onWallpapersFetched(images: LinkedHashMap<String, ArrayList<WallpaperModel>>)
        fun onFetchedCanceled(error_message: String)
    }


    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var eventListener: ValueEventListener


    private val categorizedImages = LinkedHashMap<String, ArrayList<WallpaperModel>>(6)

    init {
        categorizedImages["all_wallpapers"] = ArrayList()
        categorizedImages["cars"] = ArrayList()
        categorizedImages["nature"] = ArrayList()
        categorizedImages["sci-fi"] = ArrayList()
        categorizedImages["architectural"] = ArrayList()
        categorizedImages["others"] = ArrayList()
    }

    private fun clearData() {
        if (categorizedImages["all_wallpapers"]!!.isNotEmpty())
            for (list in categorizedImages.values)
                list.clear()
    }


    fun fetchAllWallpapers() {

        eventListener = reference
            .child("all_wallpapers")
            .orderByChild("id")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    clearData()

                    for (dss in snapshot.children)
                    try{
                        dss.getValue(WallpaperModel::class.java)?.let {

                            categorizedImages["all_wallpapers"]?.add(it)
                            categorizedImages[when (it.category) {
                                "cars" -> "cars"
                                "nature" -> "nature"
                                "sci-fi" -> "sci-fi"
                                "architectural" -> "architectural"
                                else -> "others"
                            }]?.add(it)
                        }
                    }catch (e: Exception){
                        continue
                    }



                    for (listener in getListeners())
                        listener.onWallpapersFetched(categorizedImages)

                }

                override fun onCancelled(error: DatabaseError) {
                    for (listener in getListeners())
                        listener.onFetchedCanceled(error.message)
                }
            })



        removeListeners()
    }

    private fun removeListeners() {
        reference.removeEventListener(eventListener)
    }


}