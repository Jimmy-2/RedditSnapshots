package com.example.snapshotsforreddit.ui.common.redditpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.databinding.ItemPostBinding
import com.example.snapshotsforreddit.databinding.ItemPostCompactBinding
import com.example.snapshotsforreddit.databinding.ItemSearchBarBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.ui.common.viewholders.PostCompactViewHolderTest
import com.example.snapshotsforreddit.ui.common.viewholders.PostViewHolderTest
import com.example.snapshotsforreddit.ui.common.viewholders.SearchBarViewHolderTest

class RedditPageAdapterTest (
    private val onClickListener: OnItemClickListener,
    ) : PagingDataAdapter<RedditChildrenData, RecyclerView.ViewHolder>(POST_COMPARATOR) {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            //private var isCompact = false
//
//        isCompact =  runBlocking { = preferencesFlow.first().isCompactView}
//               viewLifecycleOwner.lifecycleScope.launch {
//                   isCompact =  preferencesFlow.first().isCompactView
//               }

            return when (viewType) {
                SEARCH -> SearchBarViewHolderTest(
                    onClickListener,
                    ItemSearchBarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )


                POST -> PostViewHolderTest(
                    this@RedditPageAdapterTest, onClickListener,
                    ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
                POST_COMPACT -> PostCompactViewHolderTest(
                    this@RedditPageAdapterTest, onClickListener,
                    ItemPostCompactBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )


//                    PostViewHolder(
//                        this@RedditPageAdapter, onClickListener,
//                        ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//                    )


                else -> throw IllegalArgumentException("Error with view type")
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val currentItem = getItem(position)
            if (currentItem != null) {
                when (holder) {
                    is SearchBarViewHolderTest -> holder.bind(currentItem)
                    is PostViewHolderTest -> holder.bind(currentItem)
                    is PostCompactViewHolderTest -> holder.bind(currentItem)
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when (getItem(position)?.dataKind) {
                "search" -> SEARCH
                else -> when (getItem(0)?.isCompact) {
                    true -> POST_COMPACT
                    false -> POST
                    else -> ERROR
                }
            }
        }


        interface OnItemClickListener : PostViewHolderTest.OnItemClickListener,
            SearchBarViewHolderTest.OnItemClickListener, PostCompactViewHolderTest.OnItemClickListener {
            override fun onSearchSubmit(query: String?, subredditName: String)
            override fun onItemClick(post: RedditChildrenData)
            override fun onVoteClick(post: RedditChildrenData, type: Int)

        }

        companion object {
            private const val SEARCH = 0
            private const val POST = 1
            private const val POST_COMPACT = 2
            private const val ERROR = 3

            private val POST_COMPARATOR = object : DiffUtil.ItemCallback<RedditChildrenData>() {
                override fun areItemsTheSame(
                    oldItem: RedditChildrenData,
                    newItem: RedditChildrenData
                ): Boolean {
                    //TODO compare more variables than just name
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: RedditChildrenData,
                    newItem: RedditChildrenData
                ): Boolean {
                    return oldItem == newItem
                }
            }
        }


    }