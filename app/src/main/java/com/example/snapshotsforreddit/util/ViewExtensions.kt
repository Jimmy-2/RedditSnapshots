package com.example.snapshotsforreddit.util

import androidx.appcompat.widget.SearchView


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