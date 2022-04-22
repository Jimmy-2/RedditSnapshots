package com.example.snapshotsforreddit.ui.tabs.account.history

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject

class AccountHistoryAdapter :
    PagingDataAdapter<RedditChildrenObject, RecyclerView.ViewHolder>(
        AccountHistoryAdapter.POST_COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }




    companion object {
        // View Types
        private val COMMENT = 0
        private val POST = 1
        private val TEXT_POST = 2
        private val USER_INFO = 3
        private val DEFAULT = 4
        private val DEFAULT_TOP = 5
        private val DEFAULT_BOTTOM = 6
        private val HEADER = 7
        private val LOADING = 8
        private val ERROR = 9


        private val POST_COMPARATOR =
            object : DiffUtil.ItemCallback<RedditChildrenObject>() {
                override fun areItemsTheSame(
                    oldItem: RedditChildrenObject,
                    newItem: RedditChildrenObject
                ): Boolean {

                    //CHANGE THIS TO data.name LATER SO WE DO NOT HAVE TO UPDATE EVEN IF SUBSCRIPTION COUNT CHANGES
                    return oldItem.data.toString() == newItem.data.toString()
                }

                override fun areContentsTheSame(
                    oldItem: RedditChildrenObject,
                    newItem: RedditChildrenObject
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }


}