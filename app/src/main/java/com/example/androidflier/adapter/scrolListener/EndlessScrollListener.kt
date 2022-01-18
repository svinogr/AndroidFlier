package com.example.androidflier.adapter.scrolListener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open abstract class EndlessScrollListener(val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    private val visibleThreshold = 5
    private var currentPage = 0
    private var previousTotalItemCount = 0
    private var loading = true
    private val startingPageIndex = 0

    public fun getLastVisibleItem(lastVisiblePosition: Array<Int>): Int {
        var maxSize = 0

        for (i in 0..lastVisiblePosition.size - 1) {
            if (i == 0) maxSize = lastVisiblePosition[i]
            if (lastVisiblePosition[i] > maxSize) maxSize = lastVisiblePosition[i]
        }

        return maxSize
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        //  super.onScrolled(recyclerView, dx, dy)
        var lastVisiblePosition = 0
        val totalItemCount = layoutManager.itemCount

        lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

        if (totalItemCount < previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && (lastVisiblePosition + visibleThreshold) > totalItemCount) {
            currentPage.inc()
            //onLoadMore(currentPage, totalItemCount, recyclerView)

        }
    }


    fun resetState() {
        currentPage = startingPageIndex
        previousTotalItemCount = 0
        loading = true
    }

    abstract fun onLoadMore(currentPage: Int, totalItemCount: Int, recyclerView: RecyclerView)
}