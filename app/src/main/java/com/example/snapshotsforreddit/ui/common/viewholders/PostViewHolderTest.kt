package com.example.snapshotsforreddit.ui.common.viewholders

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.ItemPostBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.util.calculateAgeDifferenceLocalDateTime
import com.example.snapshotsforreddit.util.getShortenedValue

class PostViewHolderTest(
    private val adapter: PagingDataAdapter<RedditChildrenData, RecyclerView.ViewHolder>,
    private val onClickListener: OnItemClickListener,
    private val binding: ItemPostBinding,
    private val isOverview: Boolean? = null,
) : RecyclerView.ViewHolder(binding.root) {
    private var post: RedditChildrenData? = null
    init {
        binding.root.setOnClickListener {
            if (post != null) {
                onClickListener.onItemClick(post!!)
            }
        }

        binding.cardUpvoteButton.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (post != null) {
                    if (post!!.likes == true) {
                        post!!.likes = null
                        onClickListener.onVoteClick(post!!, 0)
                    } else {
                        post!!.likes = true
                        onClickListener.onVoteClick(post!!, 1)
                    }
                    adapter.notifyItemChanged(position)
                }
            }
        }

        binding.cardDownvoteButton.setOnClickListener {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (post != null) {
                    if (post!!.likes == false) {
                        post!!.likes = null
                        onClickListener.onVoteClick(post!!, 0)
                    } else {
                        post!!.likes = false
                        onClickListener.onVoteClick(post!!, -1)
                    }
                    adapter.notifyItemChanged(position)
                }
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    fun bind(postObject: RedditChildrenData) {
        this.post = postObject
        val currentPost = postObject
        //TODO FIX CODE HERE : REFORMAT STATEMENTS
        binding.apply {
            if (currentPost != null) {
                if(isOverview == true) {
                    layoutPost.setBackgroundColor(ContextCompat.getColor(layoutPost.context , R.color.post_background_overview))

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

                when (post?.is_self) {
                    true -> {
                        imageviewPostItem.visibility = View.GONE
                        if (currentPost.selftext == "") {
                            textviewPostItemText.visibility = View.GONE
                        } else {
                            textviewPostItemText.text = currentPost.selftext
                            textviewPostItemText.visibility = View.VISIBLE
                        }
                    }
                    else -> {
                        textviewPostItemText.visibility = View.GONE
                        imageviewPostItem.visibility = View.VISIBLE
                        if (currentPost.preview?.images?.get(0)?.source?.url != null) {
                            val imageUrl =
                                currentPost.preview.images[0].source!!.url?.replace(
                                    removePart, ""
                                )
                            Glide.with(itemView)
                                .load(imageUrl)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .error(R.drawable.ic_error)
                                .into(imageviewPostItem)
                        }
                    }
                }

                textviewPostItemSubreddit.text =
                    currentPost.subreddit.toString().replaceFirstChar { it.uppercase() }
                textviewPostItemTitle.text = currentPost.title
                textviewPostItemScore.text = getShortenedValue(currentPost.score)
                textviewPostItemCommentCount.text = getShortenedValue(currentPost.num_comments)
                val epoch = currentPost.created_utc
                if (epoch != null) {
                    textviewPostItemAge.text = calculateAgeDifferenceLocalDateTime(epoch, 1)
                }
                when (currentPost.likes) {
                    true -> {
                        textviewPostItemScore.setTextColor(Color.parseColor(upvoteColor))
                        imageArrow.setImageResource(R.drawable.ic_upvote_arrow)
                    }
                    false -> {
                        textviewPostItemScore.setTextColor(Color.parseColor(downvoteColor))
                        imageArrow.setImageResource(R.drawable.ic_downvote_arrow)
                    }

                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: RedditChildrenData)
        fun onVoteClick(post: RedditChildrenData, type: Int)

    }


    companion object {
        private const val removePart = "amp;"
        private const val upvoteColor = "#E24824"
        private const val downvoteColor = "#5250DE"
    }

}