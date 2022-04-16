package com.example.snapshotsforreddit.ui.tabs.home

import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.SubscribedDefaultItemBinding
import com.example.snapshotsforreddit.databinding.SubscribedItemBinding
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenObject

class SubscribedAdapter(private val onClickListener: OnItemClickListener) :
    PagingDataAdapter<SubscribedChildrenObject, RecyclerView.ViewHolder>(
        SUBREDDIT_COMPARATOR
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            DEFAULT -> DefaultSubredditViewHolder(
                SubscribedDefaultItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            SUBREDDIT -> SubscribedSubredditViewHolder(
                SubscribedItemBinding.inflate(
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
                is DefaultSubredditViewHolder -> holder.bind(currentItem)
                is SubscribedSubredditViewHolder -> holder.bind(currentItem)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        println("HELLOHAHA ${getItem(position)?.kind}")
        return when (getItem(position)?.kind) {
            "default" -> DEFAULT
            "t5" -> SUBREDDIT
            else -> ERROR
        }
    }


    inner class DefaultSubredditViewHolder(val binding: SubscribedDefaultItemBinding) :
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

        fun bind(subredditObject: SubscribedChildrenObject) {
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
                    textviewDefaultSubredditItemTitle.text = subredditObject.data.display_name
                    textviewDefaultSubredditItemDescription.text = subredditObject.data.public_description
                    if(subredditObject.data.display_name == "All Posts") {
                        textviewHeader.visibility = VISIBLE
                        textviewHeader.text = "FOLLOWING"
                    }
                }

            }
        }
    }

    inner class SubscribedSubredditViewHolder(val binding: SubscribedItemBinding) :
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

        fun bind(subredditObject: SubscribedChildrenObject) {
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


    companion object {
        private val DEFAULT = 0
        private val SUBREDDIT = 1
        private val ERROR = 2

        private val SUBREDDIT_COMPARATOR =
            object : DiffUtil.ItemCallback<SubscribedChildrenObject>() {
                override fun areItemsTheSame(
                    oldItem: SubscribedChildrenObject,
                    newItem: SubscribedChildrenObject
                ): Boolean {

                    //CHANGE THIS TO data.name LATER SO WE DO NOT HAVE TO UPDATE EVEN IF SUBSCRIPTION COUNT CHANGES
                    return oldItem.data == newItem.data
                }

                override fun areContentsTheSame(
                    oldItem: SubscribedChildrenObject,
                    newItem: SubscribedChildrenObject
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }


    interface OnItemClickListener {
        fun onItemClick(subreddit: SubscribedChildrenObject)
    }

}