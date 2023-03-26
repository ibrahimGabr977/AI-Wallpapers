package com.hope.igb.aiwallpapers.screen.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.hope.igb.aiwallpapers.comman.BaseFragment
import com.hope.igb.aiwallpapers.util.SharedData

class FavoriteFragment : BaseFragment(), FavoriteViewMvc.FavoritesListener {

    private lateinit var viewMvc: FavoriteViewMvc
    private lateinit var sharedData: SharedData
    private lateinit var favoriteList: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewMvc = FavoriteViewMvc(inflater, container)

        sharedData = SharedData(requireContext())



        favoriteList = ArrayList()
        favoriteList.addAll(sharedData.getFavoriteList()!!)


        if (favoriteList.isEmpty())
            viewMvc.bindEmptyFavoriteListView()


        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)


        return viewMvc.getRootView()
    }


    override fun onStart() {
        super.onStart()

        if (favoriteList.isNotEmpty())
            viewMvc.bindFavoriteList(favoriteList)

        viewMvc.registerListener(this)

    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)

    }




    override fun onImageClicked(position: Int) {
        getScreenNavigator()?.toDisplayFragmentFromFavorite(favoriteList[position])
    }

    override fun onBackClicked() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

}