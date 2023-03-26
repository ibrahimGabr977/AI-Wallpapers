package com.hope.igb.aiwallpapers.screen.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.hope.igb.aiwallpapers.R
import com.hope.igb.aiwallpapers.comman.BaseFragment
import com.hope.igb.aiwallpapers.comman.messagehelper.SystemMessageHelper
import com.hope.igb.aiwallpapers.networking.WallpaperModel
import com.hope.igb.aiwallpapers.networking.WallpaperModelHelper
import com.hope.igb.aiwallpapers.networking.firebasedatabase.FetchWallpapersUseCase
import com.hope.igb.aiwallpapers.util.CheckNetworkState

class MainFragment : BaseFragment(), MainViewMvc.Listener,
    FetchWallpapersUseCase.Listener {

    private lateinit var viewMvc: MainViewMvc
    private lateinit var fetchUseCase: FetchWallpapersUseCase
    private var category: String = "startup"
    private lateinit var images: LinkedHashMap<String, ArrayList<WallpaperModel>>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewMvc = MainViewMvc(inflater, container)


        fetchUseCase = FetchWallpapersUseCase()


        return viewMvc.getRootView()
    }


    override fun onStart() {
        super.onStart()


        bindMainView()

        viewMvc.registerListener(this)
        fetchUseCase.registerListener(this)


    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)
        fetchUseCase.unregisterListener(this)

    }


    override fun onTabSelected(tab_id: Int) {

        if (category != "startup") {
            category = when (tab_id) {
                R.id.tab_all -> "all_wallpapers"
                R.id.tab_cars -> "cars"
                R.id.tab_nature -> "nature"
                R.id.tab_scifi -> "sci-fi"
                R.id.tab_architectural -> "architectural"
                else -> "others"

            }
        }
        bindMainView()

    }



    private fun bindMainView() {
        if (!CheckNetworkState.isInternetAvailable(requireContext()))
            viewMvc.bindNoInternetConnectionView()
        else if (category == "startup") {
            fetchUseCase.fetchAllWallpapers()
        } else {
            viewMvc.bindWallpapersFromCategory(images[category]!!)
        }



    }


    override fun onTryAgainClicked() {
        bindMainView()


    }


    override fun onWallpapersFetched(images: LinkedHashMap<String, ArrayList<WallpaperModel>>) {
        this.images = images
        category = if (category == "startup") "all_wallpapers" else category
        viewMvc.bindWallpapers(images[category]!!)


    }

    override fun onFetchedCanceled(error_message: String) {
        Snackbar.make(viewMvc.getRootView(), error_message, Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.try_again)) {
                onTryAgainClicked()
            }.show()

    }

    override fun onImageClicked(position: Int) {
        val model = images[if (category == "startup") "all_wallpapers" else category]!![position]

        if (CheckNetworkState.isInternetAvailable(requireContext()))
        getScreenNavigator()?.toDisplayFragmentFromMain(
            WallpaperModelHelper.modelUrlsToString(model),
            WallpaperModelHelper.modelUsagesToString(model)
        )
        else{
            val systemMessageHelper = SystemMessageHelper(requireContext())
            systemMessageHelper.showLongMessage("please check your internet connection and try again.")
        }

    }


    override fun onFavoriteListClicked() {
        getScreenNavigator()?.toFavoriteList()
    }


}