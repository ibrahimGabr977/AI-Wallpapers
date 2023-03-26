package com.hope.igb.aiwallpapers.comman.customtablayout

import android.view.View
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.hope.igb.aiwallpapers.R
import com.hope.igb.aiwallpapers.comman.views.BaseObservableViewMvc
import java.util.*

class CustomTabLayout(rootView: View) :
    BaseObservableViewMvc<CustomTabLayout.Listener>(),
    View.OnClickListener {


    interface Listener {
        fun onTabSelected(tab_id: Int)
    }


    companion object {
        private var last_selected_tab: Int = R.id.tab_all
    }


    private val tabLayout: HorizontalScrollView
    private val tabs: HashMap<Int, TextView>

    init {
        setRootView(rootView)
        tabLayout = findViewById(R.id.tags_layout)
        tabs = HashMap()
        initTabs()
    }

    private fun initTabs() {
        tabs[R.id.tab_all] = tabLayout.findViewById(R.id.tab_all)
        tabs[R.id.tab_cars] = tabLayout.findViewById(R.id.tab_cars)
        tabs[R.id.tab_nature] = tabLayout.findViewById(R.id.tab_nature)
        tabs[R.id.tab_scifi] = tabLayout.findViewById(R.id.tab_scifi)
        tabs[R.id.tab_architectural] = tabLayout.findViewById(R.id.tab_architectural)
        tabs[R.id.tab_others] = tabLayout.findViewById(R.id.tab_others)


        tabSelectedUiChanges(last_selected_tab)


        for (tab_id in tabs.keys)
            tabs[tab_id]!!.setOnClickListener(this)
    }


    private fun tabSelectedUiChanges(selected_tab: Int) {

        val tab = tabs[selected_tab]
        if (tab != null) {
            tab.setBackgroundResource(R.drawable.selected_tagbg)
            tab.setTextColor(ResourcesCompat.getColor(getContext().resources, R.color.black, null))
        }
        if (selected_tab != last_selected_tab)
            lastSelectedTabUiChanges()
        last_selected_tab = selected_tab

    }

    private fun lastSelectedTabUiChanges() {
        val tab = tabs[last_selected_tab]
        if (tab != null) {
            tab.setBackgroundResource(R.drawable.unselected_tagbg)
            tab.setTextColor(ResourcesCompat.getColor(getContext().resources, R.color.white, null))
        }
    }

    override fun onClick(v: View) {
        if (v.id != last_selected_tab) {
            tabSelectedUiChanges(v.id)

            for (listener in getListeners()) listener.onTabSelected(v.id)
        }

    }


}