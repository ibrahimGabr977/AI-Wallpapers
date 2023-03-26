package com.hope.igb.aiwallpapers.screen.main.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView






class MyItemsMarginsDecoration(private val spacing: Int, private val spanCount: Int,
                               private val includeEdge : Boolean)
    : RecyclerView.ItemDecoration() {


    /**
     * constructor
     * spacing desirable margin size in px between the views in the recyclerView
     * column number of columns of the RecyclerView
     */


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position

        val column: Int = position % spanCount // item column


        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }


    }

}