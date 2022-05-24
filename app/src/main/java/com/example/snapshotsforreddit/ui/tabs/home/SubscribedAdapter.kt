package com.example.snapshotsforreddit.ui.tabs.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.ItemHeaderBinding
import com.example.snapshotsforreddit.databinding.ItemSubscribedBinding
import com.example.snapshotsforreddit.databinding.ItemSubscribedDefaultBinding
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenData

class SubscribedAdapter(private val onClickListener: OnItemClickListener) :
    PagingDataAdapter<SubredditChildrenData, RecyclerView.ViewHolder>(
        SUBREDDIT_COMPARATOR
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
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
                )
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
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        onClickListener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(subreddit: SubredditChildrenData) {
            //TODO FIX CODE HERE : REFORMAT STATEMENTS
            binding.apply {
                if (subreddit != null) {
                    val iconBackgroundColor = subreddit.icon_img
                    val icon = subreddit.subscribers
                    cardSubredditItem.setCardBackgroundColor(Color.parseColor(iconBackgroundColor))
                    icon?.let { imageSubredditItem.setImageResource(it) }
                    textviewDefaultSubredditItemTitle.text = subreddit.display_name
                    textviewDefaultSubredditItemDescription.text =
                        subreddit.public_description

                }
            }
        }
    }

    inner class SubscribedSubredditViewHolder(val binding: ItemSubscribedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        onClickListener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(subreddit: SubredditChildrenData) {
            val removePart = "amp;"
            //TODO FIX CODE HERE : REFORMAT STATEMENTS
            binding.apply {
                if (subreddit!= null) {
                    val currIconUrl = subreddit.community_icon
                    val iconUrl: String =
                        if (subreddit.community_icon == null && subreddit.icon_img == null) {
                            ""
                        } else if (currIconUrl == "") {
                            subreddit.icon_img!!.replace(removePart, "")
                        } else {
                            currIconUrl!!.replace(removePart, "")
                        }
                    Glide.with(itemView)
                        .load(iconUrl)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageSubredditItem)

                    textviewSubredditItemTitle.text = subreddit.display_name_prefixed
                    favoriteSubredditItem.isVisible = subreddit.user_has_favorited!!
                }

            }
        }
    }

    inner class HeaderViewHolder(val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subredditObject: SubredditChildrenData) {
            binding.apply {
                textviewHeaderSubscribed.visibility = View.VISIBLE
                textviewHeaderSubscribed.text = "FOLLOWING"
            }
        }
    }



    interface OnItemClickListener {
        fun onItemClick(subreddit: SubredditChildrenData)
    }

    companion object {
        private const val DEFAULT = 0
        private const val SUBREDDIT = 1
        private const val HEADER = 2
        private const val ERROR = 3

        private val SUBREDDIT_COMPARATOR =
            object : DiffUtil.ItemCallback<SubredditChildrenData>() {
                override fun areItemsTheSame(
                    oldItem: SubredditChildrenData,
                    newItem: SubredditChildrenData
                ): Boolean {
                    //CHANGE THIS TO data.name LATER SO WE DO NOT HAVE TO UPDATE EVEN IF SUBSCRIPTION COUNT CHANGES
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: SubredditChildrenData,
                    newItem: SubredditChildrenData
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }




}