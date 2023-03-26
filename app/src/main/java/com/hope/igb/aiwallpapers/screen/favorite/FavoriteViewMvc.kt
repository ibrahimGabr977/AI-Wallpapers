package com.hope.igb.aiwallpapers.screen.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hope.igb.aiwallpapers.R
import com.hope.igb.aiwallpapers.screen.main.MainAdapterItemViewMvc
import com.hope.igb.aiwallpapers.screen.main.util.MyItemsMarginsDecoration
import com.hope.igb.aiwallpapers.comman.views.BaseObservableViewMvc

class FavoriteViewMvc(inflater: LayoutInflater, parent: ViewGroup?) :
    BaseObservableViewMvc<FavoriteViewMvc.FavoritesListener>(),
    MainAdapterItemViewMvc.ItemListener, FavoriteAdapterItemViewMvc.ItemListener {

    interface FavoritesListener {
        fun onImageClicked(position: Int)
        fun onBackClicked()
    }


    private var noFavoritesView: ConstraintLayout? = null
    private val recyclerView: RecyclerView

    init {
        setRootView(inflater.inflate(R.layout.favorite_fragment, parent, false))

        recyclerView = findViewById(R.id.favoritesRecyclerView)

        initView()
    }


    private fun initView() {
        findViewById<ImageView>(R.id.favorite_back).setOnClickListener {
            for (listener in getListeners()) listener.onBackClicked()
        }


        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(39)
        recyclerView.itemAnimator = null
        val layoutManager = GridLayoutManager(getContext(), 3)
        recyclerView.layoutManager = layoutManager


        if (noDecorationAddedToRecyclerView())
            recyclerView.addItemDecoration(MyItemsMarginsDecoration(13, 3, false))


    }

    private fun noDecorationAddedToRecyclerView(): Boolean {
        return recyclerView.itemDecorationCount == 0
    }


    fun bindEmptyFavoriteListView() {
        recyclerView.visibility = View.INVISIBLE


        if (noFavoritesView == null)
            noFavoritesView = findViewById(R.id.no_favorites)

        noFavoritesView?.visibility = View.VISIBLE

    }


    @SuppressLint("NotifyDataSetChanged")
    fun bindFavoriteList(wallpapersUrls: ArrayList<String>) {
        noFavoritesView?.visibility = View.GONE
        noFavoritesView = null

        recyclerView.visibility = View.VISIBLE

        val favoritesAdapter = FavoriteRecyclerAdapter(wallpapersUrls, this)
        recyclerView.adapter = favoritesAdapter
        favoritesAdapter.notifyDataSetChanged()

    }

    override fun onImageClicked(position: Int) {
        for (listener in getListeners())
            listener.onImageClicked(position)
    }
}