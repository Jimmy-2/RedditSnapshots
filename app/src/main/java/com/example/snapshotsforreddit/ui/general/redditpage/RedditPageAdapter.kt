package com.example.snapshotsforreddit.ui.general.redditpage


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.databinding.ItemPostBinding
import com.example.snapshotsforreddit.databinding.ItemPostCompactBinding
import com.example.snapshotsforreddit.databinding.ItemSearchBarBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.ui.general.PostCompactViewHolder
import com.example.snapshotsforreddit.ui.general.PostViewHolder
import com.example.snapshotsforreddit.ui.general.SearchBarViewHolder


//TODO for now use notifyItemChanged(position) until db pagination is implemented

class RedditPageAdapter(
    private val onClickListener: OnItemClickListener,
) : PagingDataAdapter<RedditChildrenObject, RecyclerView.ViewHolder>(POST_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //private var isCompact = false
//
//        isCompact =  runBlocking { = preferencesFlow.first().isCompactView}
//               viewLifecycleOwner.lifecycleScope.launch {
//                   isCompact =  preferencesFlow.first().isCompactView
//               }

        return when (viewType) {
            SEARCH -> SearchBarViewHolder(
                onClickListener,
                ItemSearchBarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )


            POST -> PostViewHolder(
                this@RedditPageAdapter, onClickListener,
                ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            POST_COMPACT -> PostCompactViewHolder(
                this@RedditPageAdapter, onClickListener,
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
                is SearchBarViewHolder -> holder.bind(currentItem)
                is PostViewHolder -> holder.bind(currentItem)
                is PostCompactViewHolder -> holder.bind(currentItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.kind) {
            "search" -> SEARCH
            else -> when (getItem(0)?.defaults?.isCompact) {
                true -> POST_COMPACT
                false -> POST
                else -> ERROR
            }
        }
    }


    interface OnItemClickListener : PostViewHolder.OnItemClickListener,
        SearchBarViewHolder.OnItemClickListener, PostCompactViewHolder.OnItemClickListener {
        override fun onSearchSubmit(query: String?, subredditName: String)
        override fun onItemClick(post: RedditChildrenObject)
        override fun onVoteClick(post: RedditChildrenObject, type: Int)

    }

    companion object {
        private const val SEARCH = 0
        private const val POST = 1
        private const val POST_COMPACT = 2
        private const val ERROR = 3

        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<RedditChildrenObject>() {
            override fun areItemsTheSame(
                oldItem: RedditChildrenObject,
                newItem: RedditChildrenObject
            ): Boolean {
                //TODO compare more variables than just name
                return oldItem.data?.name == newItem.data?.name
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