package com.example.snapshotsforreddit.ui.tabs.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.data.room.cache.subscribedsubreddit.SubscribedSubreddit
import com.example.snapshotsforreddit.databinding.ItemHeaderBinding
import com.example.snapshotsforreddit.databinding.ItemSubscribedBinding
import com.example.snapshotsforreddit.databinding.ItemSubscribedDefaultBinding

class SubscribedSubredditAdapter(
    private val onSubscribedClick: (SubscribedSubreddit) -> Unit,
    private val onDefaultClick: (String) -> Unit
): ListAdapter<SubscribedSubreddit, RecyclerView.ViewHolder>(SubredditComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            DEFAULT -> DefaultSubscribedSubredditViewHolder(
                ItemSubscribedDefaultBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            SUBREDDIT -> SubscribedSubredditViewHolder(
                ItemSubscribedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onSubscribedClick = {position ->
                    val subscribedSubreddit = getItem(position)
                    if(subscribedSubreddit != null) {
                        onSubscribedClick(subscribedSubreddit)
                    }
                },
            )
            HEADER -> HeaderViewHolder(
                ItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Error with view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            when (holder) {
                is DefaultSubscribedSubredditViewHolder -> holder.bind(currentItem)
                is SubscribedSubredditViewHolder -> holder.bind(currentItem)
                is HeaderViewHolder -> holder.bind(currentItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when (getItem(position)?.dataKind) {
            "default" -> DEFAULT
            "header" -> HEADER
            else -> SUBREDDIT
        }
    }

    inner class DefaultSubscribedSubredditViewHolder(val binding: ItemSubscribedDefaultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

            binding.buttonSubredditHome.setOnClickListener {
                navigateToSubreddit("Home Feed")
            }
            binding.buttonSubredditPopular.setOnClickListener {
                navigateToSubreddit("popular")
            }
            binding.buttonSubredditAll.setOnClickListener {
                navigateToSubreddit("all")
            }
        }

        private fun navigateToSubreddit(defaultName: String) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = getItem(position)
                if (item != null) {
                    onDefaultClick(defaultName)
                }
            }
        }


        fun bind(subreddit: SubscribedSubreddit) {

            binding.apply {

            }
        }
    }

    inner class HeaderViewHolder(val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subredditObject: SubscribedSubreddit) {
            binding.apply {
                textviewHeader.visibility = View.VISIBLE
                textviewHeader.text = "FOLLOWING"
            }
        }
    }


    class SubredditComparator : DiffUtil.ItemCallback<SubscribedSubreddit>() {
        override fun areItemsTheSame(oldItem: SubscribedSubreddit, newItem: SubscribedSubreddit): Boolean {
            //if contents are the same but are at different positions, this method will know how to move that item
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: SubscribedSubreddit, newItem: SubscribedSubreddit): Boolean {
            return oldItem == newItem
        }

    }

    companion object {
        private const val DEFAULT = 0
        private const val SUBREDDIT = 1
        private const val HEADER = 2
        private const val ERROR = 3
    }
}

// Old adapter class before adding caching functionality

//class SubscribedAdapter(private val onClickListener: OnItemClickListener) :
//    PagingDataAdapter<SubredditChildrenData, RecyclerView.ViewHolder>(
//        SUBREDDIT_COMPARATOR
//    ) {
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): RecyclerView.ViewHolder {
//        return when (viewType) {
//            DEFAULT -> DefaultSubscribedSubredditViewHolder(
//                ItemSubscribedDefaultBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//            SUBREDDIT -> SubscribedSubredditViewHolder(
//                ItemSubscribedBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//            HEADER -> HeaderViewHolder(
//                ItemHeaderBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//            else -> throw IllegalArgumentException("Error with view type")
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val currentItem = getItem(position)
//        if (currentItem != null) {
//            when (holder) {
//                is DefaultSubscribedSubredditViewHolder -> holder.bind(currentItem)
//                is SubscribedSubredditViewHolder -> holder.bind(currentItem)
//                is HeaderViewHolder -> holder.bind(currentItem)
//            }
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//
//        return when (getItem(position)?.dataKind) {
//            "default" -> DEFAULT
//            "header" -> HEADER
//            else -> SUBREDDIT
//        }
//    }
//
//    inner class DefaultSubscribedSubredditViewHolder(val binding: ItemSubscribedDefaultBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        init {
//
//            binding.buttonSubredditHome.setOnClickListener {
//                navigateToSubreddit("Home")
//            }
//            binding.buttonSubredditPopular.setOnClickListener {
//                navigateToSubreddit("popular")
//            }
//            binding.buttonSubredditAll.setOnClickListener {
//                navigateToSubreddit("all")
//            }
//        }
//
//        private fun navigateToSubreddit(defaultName: String) {
//            val position = bindingAdapterPosition
//            if (position != RecyclerView.NO_POSITION) {
//                val item = getItem(position)
//                if (item != null) {
//                    onClickListener.onDefaultClick(defaultName)
//                }
//            }
//        }
//
//
//        fun bind(subreddit: SubredditChildrenData) {
//
//            binding.apply {
//
//            }
//        }
//    }
//
//    inner class SubscribedSubredditViewHolder(val binding: ItemSubscribedBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        init {
//            binding.root.setOnClickListener {
//                val position = bindingAdapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    val item = getItem(position)
//                    if (item != null) {
//                        onClickListener.onSubredditClick(item)
//                    }
//                }
//            }
//        }
//
//        fun bind(subreddit: SubredditChildrenData) {
//            val removePart = "amp;"
//            binding.apply {
//                val iconUrl: String =
//                    if (subreddit.community_icon != "" && subreddit.community_icon != null) {
//                        subreddit.community_icon.replace(removePart, "")
//                    } else if (subreddit.icon_img != "" && subreddit.icon_img != null) {
//                        subreddit.icon_img.replace(removePart, "")
//                    } else {
//                        ""
//                    }
//                Glide.with(itemView)
//                    .load(iconUrl)
//                    .centerCrop()
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .error(R.drawable.ic_blank)
//                    .into(imageSubredditIcon)
//
//
//                if(subreddit.primary_color != null && subreddit.primary_color != "") {
//                    imageSubredditIcon.setBackgroundColor(Color.parseColor(subreddit.primary_color))
//                }
//
//                textviewSubredditItemTitle.text = subreddit.display_name_prefixed
//                favoriteSubredditItem.isVisible = subreddit.user_has_favorited!!
//
//            }
//        }
//    }
//
//    inner class HeaderViewHolder(val binding: ItemHeaderBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(subredditObject: SubredditChildrenData) {
//            binding.apply {
//                textviewHeader.visibility = View.VISIBLE
//                textviewHeader.text = "FOLLOWING"
//            }
//        }
//    }
//
//
//
//    interface OnItemClickListener {
//        fun onDefaultClick(defaultName: String)
//        fun onSubredditClick(subreddit: SubredditChildrenData)
//    }
//
//    companion object {
//        private const val DEFAULT = 0
//        private const val SUBREDDIT = 1
//        private const val HEADER = 2
//        private const val ERROR = 3
//
//        private val SUBREDDIT_COMPARATOR =
//            object : DiffUtil.ItemCallback<SubredditChildrenData>() {
//                override fun areItemsTheSame(
//                    oldItem: SubredditChildrenData,
//                    newItem: SubredditChildrenData
//                ): Boolean {
//                    return oldItem.name == newItem.name
//                }
//
//                override fun areContentsTheSame(
//                    oldItem: SubredditChildrenData,
//                    newItem: SubredditChildrenData
//                ): Boolean {
//                    return oldItem == newItem
//                }
//
//            }
//    }
//
//
//
//
//}