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
import com.example.snapshotsforreddit.databinding.ItemPostCompactBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenData
import com.example.snapshotsforreddit.util.calculateAgeDifferenceLocalDateTime
import com.example.snapshotsforreddit.util.getShortenedValue

class PostCompactViewHolder (
    private val adapter: PagingDataAdapter<RedditChildrenData, RecyclerView.ViewHolder>,
    private val onClickListener: OnItemClickListener,
    private val binding: ItemPostCompactBinding,
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
    fun bind(post: RedditChildrenData) {
        this.post = post
        val currentPost = post
        //TODO FIX CODE HERE : REFORMAT STATEMENTS
        binding.apply {
            if(isOverview == true) {
                layoutPostCompact.setBackgroundColor(
                    ContextCompat.getColor(layoutPostCompact.context ,
                        R.color.post_background_overview))

            }
            //Subreddit icon
            val subreddit = post.sr_detail
            if(subreddit != null) {
                val iconUrl: String =
                    if (subreddit.community_icon != "" && subreddit.community_icon != null) {
                        subreddit.community_icon.replace(removePart, "")
                    } else if (subreddit.icon_img != "" && subreddit.icon_img != null) {
                        subreddit.icon_img.replace(removePart, "")
                    } else {
                        ""
                    }
                Glide.with(itemView)
                    .load(iconUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_blank)
                    .into(imageviewSubredditIcon)

                if(subreddit.primary_color != null && subreddit.primary_color != "") {
                    imageviewSubredditIcon.setBackgroundColor(Color.parseColor(subreddit.primary_color))
                }
            }


            imageviewPostItem.visibility = View.VISIBLE
            if (post.is_self == true ||  post.preview == null) {
                    imageviewPostItem.setImageResource(R.drawable.ic_text)
                }
                else {
                    if (currentPost.preview?.images?.get(0)?.source?.url != null) {
                        val imageUrl =
                            currentPost.preview.images[0].source!!.url?.replace(
                                removePart, ""
                            )
                        Glide.with(itemView)
                            .load(imageUrl)
                            .centerCrop()
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.ic_error)
                            .into(imageviewPostItem)
                    }
                }


            textviewPostItemSubreddit.text =
                currentPost.subreddit.toString().replaceFirstChar { it.uppercase() }
            textviewPostItemTitle.text = currentPost.title
            textviewPostItemScore.text = getShortenedValue(currentPost.score)
            textviewPostItemCommentCount.text =
                getShortenedValue(currentPost.num_comments)
            val epoch = currentPost.created_utc
            if (epoch != null) {
                textviewPostItemAge.text = calculateAgeDifferenceLocalDateTime(epoch, 1)
            }
            if(currentPost.saved == true) {
                imageSaved.visibility = View.VISIBLE
            }else {
                imageSaved.visibility = View.GONE
            }
            when (currentPost.likes) {
                true -> {
                    imageUpvoteLayout.setBackgroundColor(Color.parseColor(upvoteColor))
                    imageUpvoteButton.setImageResource(R.drawable.ic_up_arrow_white)

                    imageDownvoteButton.setImageResource(R.drawable.ic_down_arrow_null)
                    imageDownvoteLayout.setBackgroundColor(Color.TRANSPARENT)


                    textviewPostItemScore.setTextColor(Color.parseColor(upvoteColor))
                    imageArrow.setImageResource(R.drawable.ic_upvote_arrow)
                }
                false -> {
                    imageDownvoteLayout.setBackgroundColor(Color.parseColor(downvoteColor))
                    imageDownvoteButton.setImageResource(R.drawable.ic_down_arrow_white)

                    imageUpvoteButton.setImageResource(R.drawable.ic_up_arrow_null)
                    imageUpvoteLayout.setBackgroundColor(Color.TRANSPARENT)

                    textviewPostItemScore.setTextColor(Color.parseColor(downvoteColor))
                    imageArrow.setImageResource(R.drawable.ic_downvote_arrow)
                }
                null -> {
                    imageUpvoteLayout.setBackgroundColor(Color.TRANSPARENT)
                    imageDownvoteLayout.setBackgroundColor(Color.TRANSPARENT)
                    textviewPostItemScore.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Small)
                    imageArrow.setImageResource(R.drawable.ic_up_arrow_null)
                    imageUpvoteButton.setImageResource(R.drawable.ic_up_arrow_null)
                    imageDownvoteButton.setImageResource(R.drawable.ic_down_arrow_null)
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