 package com.jimmywu.snapshotsforreddit.util

import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


//extension function for search view
inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
            //triggered whenever we click the submit button. more useful for searches that require api
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            //triggered whenever we type something in the search view (filters in real time)
            listener(newText.orEmpty())
            return true
        }

    })
}


fun changeViewOnLoadState(
    loadState: CombinedLoadStates,
    adapterItemCount: Int,
    minCount: Int,
    progressBar: ProgressBar,
    recyclerView: RecyclerView,
    buttonRetry: Button,
    textViewError: TextView,
    textViewEmpty: TextView,
    refresh: SwipeRefreshLayout
) {
    //if results are loading/ finished loading
    progressBar.isVisible = loadState.source.refresh is LoadState.Loading
    //recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
    refresh.isRefreshing = loadState.mediator?.refresh is LoadState.Loading

    //
    buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
    textViewError.isVisible = loadState.source.refresh is LoadState.Error
    recyclerView.isVisible = !textViewError.isVisible

    if(loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapterItemCount <= minCount ) {
        recyclerView.isVisible = false
        textViewEmpty.isVisible = true
    }else {
        textViewEmpty.isVisible = false
    }
}

