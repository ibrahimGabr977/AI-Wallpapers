package com.hope.igb.aiwallpapers.screen.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hope.igb.aiwallpapers.networking.WallpaperModel

class MainRecyclerAdapter(

    private val wallpaperModels: ArrayList<WallpaperModel>,
    private val listener: MainAdapterItemViewMvc.ItemListener
) :
    RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {


    class MainViewHolder(val itemViewMvc: MainAdapterItemViewMvc) :
        ViewHolder(itemViewMvc.getRootView())


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemViewMvc = MainAdapterItemViewMvc(
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


        holder.itemViewMvc.bindAdapterItem(wallpaperModels[position], position)

    }


    override fun getItemCount(): Int {
        return wallpaperModels.size
    }


    var currentFilter ="recent"

    @SuppressLint("NotifyDataSetChanged")
    fun reSortDataBy(filter: String) {

        wallpaperModels.sortWith { data1, data2 ->
            if (filter == "recent")
                if (data1.id.toLong() > data2.id.toLong()) 1
                else if (data1.id.toLong() < data2.id.toLong()) -1 else 0
            else
                if (data1.usageTimes > data2.usageTimes) -1
                else if (data1.usageTimes < data2.usageTimes) 1 else 0

        }

        notifyDataSetChanged()
    }


}