package com.example.snapshotsforreddit.ui.general.redditpage

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.PostItemBinding
import com.example.snapshotsforreddit.databinding.PostTextItemBinding


import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import java.time.*


//TODO for now use notifyItemChanged(position) until db pagination is implemented
class RedditPageAdapter(private val onClickListener: OnItemClickListener):
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
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        onClickListener.onItemClick(post)
                    }
                }
            }
            binding.cardUpvoteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        if(post.data?.likes == true) {
                            post.data.likes = null
                            onClickListener.onVoteClick(post, 0, position)
                        }else {
                            post.data?.likes = true
                            onClickListener.onVoteClick(post, 1, position)
                        }

                        notifyItemChanged(position)
                    }
                }
            }
            binding.cardDownvoteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        if(post.data?.likes == false) {
                            post.data.likes = null
                            onClickListener.onVoteClick(post, 0, position)
                        }else {
                            post.data?.likes = false
                            onClickListener.onVoteClick(post, -1, position)
                        }

                        notifyItemChanged(position)
                    }
                }
            }
        }


        @SuppressLint("ResourceAsColor")
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
            val removePart = "amp;"
            //TODO FIX CODE HERE : REFORMAT STATEMENTS
            binding.apply {
                //if true, is text only post
                if(currentPost != null) {
                    if (currentPost.preview?.images?.get(0)?.source?.url != null) {
                        val imageUrl =
                            currentPost.preview.images[0].source!!.url?.replace(removePart, "")
                        Glide.with(itemView)
                            .load(imageUrl)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.ic_error)
                            .into(imageviewPostItem)
                    }
                    //Subreddit icon
                    val currIconUrl = currentPost.sr_detail?.community_icon.toString()
                    val iconUrl: String =
                        if (currentPost.sr_detail?.community_icon == null && currentPost.sr_detail?.icon_img == null) {
                            ""
                        } else if (currIconUrl == "null") {

                            currentPost.sr_detail.icon_img!!.replace(removePart, "")
                        } else {
                            currIconUrl.replace(removePart, "")
                        }
                    Glide.with(itemView)
                        .load(iconUrl)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageviewSubredditIcon)

                    textviewPostItemSubreddit.text = currentPost.subreddit.toString().replaceFirstChar { it.uppercase() }
                    textviewPostItemTitle.text = currentPost.title
                    textviewPostItemScore.text = getShortenedValue(currentPost.score)
                    textviewPostItemCommentCount.text = getShortenedValue(currentPost.num_comments)
                    val epoch = currentPost.created_utc
                    if (epoch != null) {
                        textviewPostItemAge.text = calculateAgeDifferenceLocalDateTime(epoch, 1)
                    }

                    //upvote/downvote related

                    val upvoteColor = "#E24824"
                    val downvoteColor = "#5250DE"
                    when (currentPost.likes) {
                        true -> {
                            textviewPostItemScore.setTextColor(Color.parseColor(upvoteColor))
                            imageArrow.setImageResource(R.drawable.ic_upvote_arrow)
                        }
                        false -> {
                            textviewPostItemScore.setTextColor(Color.parseColor(downvoteColor))
                            imageArrow.setImageResource(R.drawable.ic_downvote_arrow)
                        }
                        null -> {
                            textviewPostItemScore.setTextColor(coil.base.R.color.androidx_core_secondary_text_default_material_light)
                            imageArrow.setImageResource(R.drawable.ic_up_arrow_null)
                        }
                    }




                }
            }
        }
    }





    inner class TextPostViewHolder(private val binding: PostTextItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        onClickListener.onItemClick(post)
                    }
                }
            }
            binding.cardUpvoteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        if(post.data?.likes == true) {
                            post.data.likes = null
                            onClickListener.onVoteClick(post, 0, position)
                        }else {
                            post.data?.likes = true
                            onClickListener.onVoteClick(post, 1, position)
                        }
                        notifyItemChanged(position)

                    }
                }
            }
            binding.cardDownvoteButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val post = getItem(position)
                    if (post != null) {
                        if(post.data?.likes == false) {
                            post.data.likes = null
                            onClickListener.onVoteClick(post, 0, position)
                        }else {
                            post.data?.likes = false
                            onClickListener.onVoteClick(post, -1, position)
                        }
                        notifyItemChanged(position)


                    }
                }
            }
        }


        @SuppressLint("ResourceAsColor")
        fun bind(postObject: RedditChildrenObject) {
            val currentPost = postObject.data
            val removePart = "amp;"
            binding.apply {
                if (currentPost != null) {
                    val currIconUrl = currentPost.sr_detail?.community_icon.toString()
                    val iconUrl: String =
                        if (currentPost.sr_detail?.community_icon == null && currentPost.sr_detail?.icon_img == null) {
                            ""
                        } else if (currIconUrl == "null") {
                            currentPost.sr_detail.icon_img!!.replace(removePart, "")
                        } else {
                            currIconUrl.replace(removePart, "")
                        }
                    Glide.with(itemView)
                        .load(iconUrl)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageviewSubredditIcon)

                    textviewPostItemSubreddit.text = currentPost.subreddit.toString().replaceFirstChar { it.uppercase() }
                    textviewPostItemTitle.text = currentPost.title
                    if(currentPost.selftext == "") {
                        textviewPostItemText.visibility = GONE
                    }else {
                        textviewPostItemText.text = currentPost.selftext
                    }

                    textviewPostItemScore.text = getShortenedValue(currentPost.score)
                    textviewPostItemCommentCount.text = getShortenedValue(currentPost.num_comments)
                    val epoch = currentPost.created_utc
                    if (epoch != null) {
                        textviewPostItemAge.text = calculateAgeDifferenceLocalDateTime(epoch, 1)
                    }



                    val upvoteColor = "#E24824"
                    val downvoteColor = "#5250DE"
                    when (currentPost.likes ) {
                        true -> {
                            textviewPostItemScore.setTextColor(Color.parseColor(upvoteColor))
                            imageArrow.setImageResource(R.drawable.ic_upvote_arrow)
                        }
                        false -> {
                            textviewPostItemScore.setTextColor(Color.parseColor(downvoteColor))
                            imageArrow.setImageResource(R.drawable.ic_downvote_arrow)
                        }
                        null -> {
                            textviewPostItemScore.setTextColor(coil.base.R.color.androidx_core_secondary_text_default_material_light)
                            imageArrow.setImageResource(R.drawable.ic_up_arrow_null)
                        }
                    }
                }

            }
        }
    }


    interface OnItemClickListener {
        fun onItemClick(post: RedditChildrenObject)
        fun onVoteClick(post: RedditChildrenObject, type: Int, position: Int)
    }

    companion object {

        private const val POST = 0
        private const val TEXT_POST = 1
        private const val ERROR = 2

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