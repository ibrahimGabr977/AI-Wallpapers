package com.hope.igb.aiwallpapers.screen.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class FavoriteRecyclerAdapter(

    private val wallpapersUrls: ArrayList<String>,
    private val listener: FavoriteAdapterItemViewMvc.ItemListener
) :
    RecyclerView.Adapter<FavoriteRecyclerAdapter.MainViewHolder>() {




    class MainViewHolder(val itemViewMvc: FavoriteAdapterItemViewMvc) :
        ViewHolder(itemViewMvc.getRootView())




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemViewMvc = FavoriteAdapterItemViewMvc(
            LayoutInflater.from(parent.context),
            parent
        )
        return MainViewHolder(itemViewMvc)


    }


    override fun onViewDetachedFromWindow(holder: MainViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.itemViewMvc.unregisterListener(listener)


    }


    override fun onViewAttachedToWindow(holder: MainViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.itemViewMvc.registerListener(listener)

    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {


        holder.itemViewMvc.bindAdapterItem(wallpapersUrls[position], position)

    }


    override fun getItemCount(): Int {
        return wallpapersUrls.size
    }





}