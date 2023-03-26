package com.hope.igb.aiwallpapers.screen.favorite

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.hope.igb.aiwallpapers.R
import com.hope.igb.aiwallpapers.util.ImageLoader
import com.hope.igb.aiwallpapers.comman.views.BaseObservableViewMvc
import com.hope.igb.aiwallpapers.networking.WallpaperModelHelper

class FavoriteAdapterItemViewMvc (inflater: LayoutInflater, parent: ViewGroup)
    : BaseObservableViewMvc<FavoriteAdapterItemViewMvc.ItemListener>() {



    interface ItemListener {
        fun onImageClicked(position: Int)

    }


    private var  imageView:ImageView
    private var imageLoader: ImageLoader


    init {
        setRootView(inflater.inflate(R.layout.image_holder, parent, false))

        val params = getRootView().layoutParams as ViewGroup.LayoutParams


        val itemWidth = ((getScreenWidth() * 0.95 / 3.0) - 13.0).toInt()



        params.height = (itemWidth * 3)/2



        getRootView().layoutParams = params

        imageView = findViewById(R.id.image_holder_image)


        imageLoader = ImageLoader(getContext())



    }

    private fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }


    fun bindAdapterItem(wallpaperUrls: String, position: Int){


       imageLoader.loadImage(
           WallpaperModelHelper.getOriginalUrl(wallpaperUrls)
       ).into(imageView)




         getRootView().setOnClickListener {

             for (listener in getListeners())
                 listener.onImageClicked(position)
         }

     }




}