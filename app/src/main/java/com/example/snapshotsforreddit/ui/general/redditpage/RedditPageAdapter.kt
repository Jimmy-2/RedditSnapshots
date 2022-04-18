package com.example.snapshotsforreddit.ui.general.redditpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.AccountCommentItemBinding
import com.example.snapshotsforreddit.databinding.PostItemBinding
import com.example.snapshotsforreddit.databinding.PostTextItemBinding


import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.ui.tabs.account.AccountOverviewAdapter
import retrofit2.http.POST
import java.time.*

class RedditPageAdapter() :
    PagingDataAdapter<RedditChildrenObject, RecyclerView.ViewHolder>(
        POST_COMPARATOR
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return when (viewType) {
            TEXT_POST -> TextPostViewHolder(
                PostTextItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            POST -> PostViewHolder(
                PostItemBinding.inflate(
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
                is TextPostViewHolder -> holder.bind(currentItem)
                is PostViewHolder -> holder.bind(currentItem)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.data?.is_self) {
            true -> TEXT_POST
            false -> POST
            else -> ERROR
        }

    }


    inner class PostViewHolder(private val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
            val removePart = "amp;"
            //TODO FIX CODE HERE : REFORMAT STATEMENTS
            binding.apply {
                //if true, is text only post
                if (currentPost!!.is_self == true) {
                    imageviewPostItem.setImageResource(0);
                } else {
                    //TODO REDO THIS TO ALLOW GIFS AND VIDEOS
                    if (currentPost.preview?.images?.get(0)?.source?.url != null) {
                        val iconUrl = currentPost.preview.images[0].source!!.url?.replace(removePart, "")
                        Glide.with(itemView)
                            .load(iconUrl)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.ic_error)
                            .into(imageviewPostItem)
                    }


                }

                textviewPostItemSubreddit.text = currentPost.subreddit
                textviewPostItemTitle.text = currentPost.title
                textviewPostItemScore.text = getShortenedValue(currentPost.score)
                textviewPostItemCommentCount.text = getShortenedValue(currentPost.num_comments)
                val epoch = currentPost.created_utc
                if (epoch != null) {
                    textviewPostItemAge.text = calculateAgeDifferenceLocalDateTime(epoch, 1)
                }
            }
        }
    }

    inner class TextPostViewHolder(private val binding: PostTextItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
            binding.apply {
                if (currentPost != null) {
                    textviewPostItemSubreddit.text = currentPost.subreddit
                    textviewPostItemTitle.text = currentPost.title
                    textviewPostItemText.text = currentPost.selftext
                    textviewPostItemScore.text = getShortenedValue(currentPost.score)
                    textviewPostItemCommentCount.text = getShortenedValue(currentPost.num_comments)
                    val epoch = currentPost.created_utc
                    if (epoch != null) {
                        textviewPostItemAge.text = calculateAgeDifferenceLocalDateTime(epoch, 1)
                    }
                }

            }
        }
    }

    companion object {
        private val TEXT_POST = 0
        private val POST = 1
        private val ERROR = 2

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
    private fun getShortenedValue(value: Int?): String {
        return when {
            value == null -> {
                ""
            }
            value < 1000 -> {
                value.toString()
            }
            value < 1000000 -> {
                String.format("%.1f", (value.toDouble()/1000)) + "K"
            }
            else -> {
                String.format("%.1f", (value.toDouble()/1000000)) + "M"
            }
        }
    }

    private fun calculateAgeDifferenceLocalDateTime(epoch: Long, type: Int): String {
        val createdOn = epoch.let { Instant.ofEpochMilli(it*1000).atZone(ZoneId.systemDefault()).toLocalDate() }
        val ageMY = Period.between(createdOn, LocalDate.now())
        return when {
            //if both years and months == 0, we measure account age by days, hours, and minutes instead
            ageMY.years == 0 && ageMY.months == 0 -> {
                val createdOn = epoch.let { Instant.ofEpochMilli(it*1000).atZone(ZoneId.systemDefault()).toLocalDateTime() }
                //Days, hours, minutes, and seconds
                val ageDHMS = Duration.between(createdOn, LocalDateTime.now())
                return when {
                    ageDHMS.toHours() < 24 -> {
                        if(ageDHMS.toHours() < 1) {
                            if(ageDHMS.toMinutes() < 1) {
                                return "${ageDHMS.seconds}s"
                            }else {
                                return "${ageDHMS.toMinutes()}m"
                            }
                        }else {
                            return "${ageDHMS.toHours()}h"
                        }
                    }
                    else -> "${ageDHMS.toHours()/24}d"
                }
            }
            ageMY.years == 0 -> "${ageMY.months}mo"
            ageMY.months == 0 -> "${ageMY.years}y "
            //type 1 = account age, type 0 = comments
            else -> if(type == 1) {
                "${ageMY.years}y ${ageMY.months}mo"
            }else {
                //comments older than or equal to 1 year are formatted as decimals
                "${String.format("%.1f", ((ageMY.years*12.toDouble())+ ageMY.months.toDouble())/12)}y"
            }
        }
    }

}