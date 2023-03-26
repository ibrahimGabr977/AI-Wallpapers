package com.hope.igb.aiwallpapers.screen.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hope.igb.aiwallpapers.R
import com.hope.igb.aiwallpapers.networking.WallpaperModel
import com.hope.igb.aiwallpapers.screen.main.util.MyItemsMarginsDecoration
import com.hope.igb.aiwallpapers.comman.views.BaseObservableViewMvc
import com.hope.igb.aiwallpapers.comman.customtablayout.CustomTabLayout

@SuppressLint("NotifyDataSetChanged")
class MainViewMvc(inflater: LayoutInflater, parent: ViewGroup?) :
    BaseObservableViewMvc<MainViewMvc.Listener>(), MainAdapterItemViewMvc.ItemListener {


    interface Listener : CustomTabLayout.Listener {
        fun onTryAgainClicked()
        override fun onTabSelected(tab_id: Int)
        fun onImageClicked(position: Int)
        fun onFavoriteListClicked()

    }

    private var noInternetView: ConstraintLayout? = null
    private val recyclerView: RecyclerView
    private lateinit var wallpapersAdapter: MainRecyclerAdapter
    private val tagsLayout: CustomTabLayout
    private val filterRecent: TextView
    private val filterPopular: TextView
    private var progressBar: ProgressBar? = null
    private var currentFilter: String = "recent"

    init {
        setRootView(inflater.inflate(R.layout.main_fragment, parent, false))
        showProgressBar()

        tagsLayout = CustomTabLayout(getRootView())
        filterRecent = findViewById(R.id.filter_recent)
        filterPopular = findViewById(R.id.filter_popular)
        recyclerView = findViewById(R.id.mainRecyclerView)




        initView()


    }


    private fun initView() {
        initFilters()
        findViewById<ImageView>(R.id.favorite_list).setOnClickListener {
            for (listener in getListeners())
                listener.onFavoriteListClicked()
        }

        initListView()

    }


    private fun initFilters() {

        filterRecent.setOnClickListener {
            if (currentFilter != "recent")
                onRecentFilterClicked()

        }

        filterPopular.setOnClickListener {
            if (currentFilter != "popular")
                onPopularFilterClicked()
        }

    }

    private fun onRecentFilterClicked() {
        if (dataListIsNotEmpty()) {
            setCurrentFilter("recent")
            wallpapersAdapter.reSortDataBy("recent")

        }

    }


    private fun onPopularFilterClicked() {
        if (dataListIsNotEmpty()) {
            setCurrentFilter("popular")
            wallpapersAdapter.reSortDataBy("popular")

        }


    }


    private fun initListView() {
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


    fun bindNoInternetConnectionView() {
        recyclerView.visibility = View.INVISIBLE

        if (noInternetView == null)
            noInternetView = findViewById(R.id.no_internet_connection)


        noInternetView!!.visibility = View.VISIBLE
        noInternetView!!.findViewById<TextView>(R.id.try_again).setOnClickListener {
            for (listener in getListeners())
                listener.onTryAgainClicked()
        }

        hideProgressBar()
    }

    fun bindWallpapers(wallpapers: ArrayList<WallpaperModel>) {

        noInternetView?.visibility = View.GONE
        noInternetView = null

        recyclerView.visibility = View.VISIBLE

        wallpapersAdapter = MainRecyclerAdapter(wallpapers, this)
        recyclerView.adapter = wallpapersAdapter
        wallpapersAdapter.notifyDataSetChanged()
        hideProgressBar()
    }


    fun bindWallpapersFromCategory(categorizedWallpapers: ArrayList<WallpaperModel>) {
        recyclerView.visibility = View.VISIBLE
        wallpapersAdapter = MainRecyclerAdapter(categorizedWallpapers, this)
        recyclerView.swapAdapter(wallpapersAdapter, false)
        wallpapersAdapter.notifyDataSetChanged()
        setCurrentFilter(wallpapersAdapter.currentFilter)
        hideProgressBar()

    }


    private fun setCurrentFilter(filter: String) {
        if (filter == "recent") {
            filterPopular.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_color))
            filterRecent.setTextColor(ContextCompat.getColor(getContext(), R.color.white))
        } else {
            filterRecent.setTextColor(ContextCompat.getColor(getContext(), R.color.gray_color))
            filterPopular.setTextColor(ContextCompat.getColor(getContext(), R.color.white))
        }
        currentFilter = "" + filter

    }

    private fun dataListIsNotEmpty(): Boolean {
        return recyclerView.childCount != 0
    }


    override fun registerListener(listener: Listener) {
        super.registerListener(listener)
        tagsLayout.registerListener(listener)
    }

    override fun unregisterListener(listener: Listener) {
        super.unregisterListener(listener)
        tagsLayout.unregisterListener(listener)
    }

    override fun onImageClicked(position: Int) {
        for (listener in getListeners())
            listener.onImageClicked(position)
    }

    private fun showProgressBar() {
        if (progressBar == null)
            progressBar = findViewById(R.id.progressBar)

        progressBar?.visibility = View.VISIBLE


    }


    private fun hideProgressBar() {
        progressBar?.visibility = View.GONE
        progressBar = null

    }
}