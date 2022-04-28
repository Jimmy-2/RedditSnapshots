package com.example.snapshotsforreddit.ui.tabs.home

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
import com.example.snapshotsforreddit.network.responses.subreddit.SubredditChildrenObject
import com.example.snapshotsforreddit.ui.tabs.account.overview.AccountOverviewAdapter.HeaderViewHolder

class SubscribedAdapter(private val onClickListener: OnItemClickListener) :
    PagingDataAdapter<SubredditChildrenObject, RecyclerView.ViewHolder>(
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

        return when (getItem(position)?.kind) {
            "default" -> DEFAULT
            "t5" -> SUBREDDIT
            "header" -> HEADER
            else -> ERROR
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

        fun bind(subredditObject: SubredditChildrenObject) {
            val removePart = "amp;"
            //TODO FIX CODE HERE : REFORMAT STATEMENTS
            binding.apply {
                if (subredditObject.data != null) {
                    val currIconUrl = subredditObject.data.community_icon.toString()
                    val iconUrl: String =
                        if (subredditObject.data.community_icon == null && subredditObject.data.icon_img == null) {
                            ""
                        } else if (currIconUrl == "") {
                            subredditObject.data.icon_img!!.replace(removePart, "")
                        } else {
                            currIconUrl.replace(removePart, "")
                        }
                    Glide.with(itemView)
                        .load(iconUrl)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageSubredditItem)

                    textviewDefaultSubredditItemTitle.text = subredditObject.data.display_name
                    textviewDefaultSubredditItemDescription.text =
                        subredditObject.data.public_description

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

        fun bind(subredditObject: SubredditChildrenObject) {
            val removePart = "amp;"
            //TODO FIX CODE HERE : REFORMAT STATEMENTS
            binding.apply {
                val currIconUrl = subredditObject.data?.community_icon
                val iconUrl: String =
                    if (subredditObject.data?.community_icon == null && subredditObject.data?.icon_img == null) {
                        ""
                    } else if (currIconUrl == "") {
                        subredditObject.data.icon_img!!.replace(removePart, "")
                    } else {
                        currIconUrl!!.replace(removePart, "")
                    }
                Glide.with(itemView)
                    .load(iconUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageSubredditItem)
                if (subredditObject.data != null) {
                    textviewSubredditItemTitle.text = subredditObject.data.display_name_prefixed
                    favoriteSubredditItem.isVisible = subredditObject.data.user_has_favorited!!
                }

            }
        }
    }

    inner class HeaderViewHolder(val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(subredditObject: SubredditChildrenObject) {
            binding.apply {
                textviewHeaderSubscribed.visibility = View.VISIBLE
                textviewHeaderSubscribed.text = "FOLLOWING"
            }
        }
    }



    interface OnItemClickListener {
        fun onItemClick(subreddit: SubredditChildrenObject)
    }

    companion object {
        private const val DEFAULT = 0
        private const val SUBREDDIT = 1
        private const val HEADER = 2
        private const val ERROR = 3

        private val SUBREDDIT_COMPARATOR =
            object : DiffUtil.ItemCallback<SubredditChildrenObject>() {
                override fun areItemsTheSame(
                    oldItem: SubredditChildrenObject,
                    newItem: SubredditChildrenObject
                ): Boolean {
                    //CHANGE THIS TO data.name LATER SO WE DO NOT HAVE TO UPDATE EVEN IF SUBSCRIPTION COUNT CHANGES
                    return oldItem.data == newItem.data
                }

                override fun areContentsTheSame(
                    oldItem: SubredditChildrenObject,
                    newItem: SubredditChildrenObject
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }




}