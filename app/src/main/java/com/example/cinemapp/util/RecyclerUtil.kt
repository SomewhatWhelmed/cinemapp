package com.example.cinemapp.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.isEndOfScroll(): Boolean {
    return layoutManager?.let {
        val gridLM = layoutManager as GridLayoutManager
        gridLM.itemCount - gridLM.findLastVisibleItemPosition() < gridLM.childCount
    } ?: false
}