package com.jimmywu.snapshotsforreddit.ui.common.viewholders

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jimmywu.snapshotsforreddit.R

import com.jimmywu.snapshotsforreddit.databinding.ItemSubredditPostBinding
import com.jimmywu.snapshotsforreddit.network.responses.RedditChildrenData
import com.jimmywu.snapshotsforreddit.util.calculateAgeDifferenceLocalDateTime
import com.jimmywu.snapshotsforreddit.util.getShortenedValue

class SubredditPostViewHolder (
    private val adapter: PagingDataAdapter<RedditChildrenData, RecyclerView.ViewHolder>,
    private val onClickListener: OnItemClickListener,
    private val binding: ItemSubredditPostBinding,
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
//                        post!!.likes = null
                        onClickListener.onVoteClick(post!!, 0)
                    } else {
//                        post!!.likes = true
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
//                        post!!.likes = null
                        onClickListener.onVoteClick(post!!, 0)
                    } else {
//                        post!!.likes = false
                        onClickListener.onVoteClick(post!!, -1)
                    }
                    adapter.notifyItemChanged(position)
                }
            }
        }

        binding.imageMoreButton.setOnClickListener{
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (post != null) {
                    onClickListener.onMoreClick(post!!, 0)
                }
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    fun bind(post: RedditChildrenData) {
        this.post = post
        //TODO FIX CODE HERE : REFORMAT STATEMENTS
        binding.apply {
            if (isOverview == true) {
                layoutPost.setBackgroundColor(
                    ContextCompat.getColor(
                        layoutPost.context,
                        R.color.post_background_overview
                    )
                )

            }

            //Image
            if (this@SubredditPostViewHolder.post?.is_self == true || this@SubredditPostViewHolder.post?.preview == null) {

                imageviewPostItem.visibility = View.GONE
                if (post.selftext == "") {
                    textviewPostItemText.visibility = View.GONE
                } else {
                    textviewPostItemText.text = post.selftext
                    textviewPostItemText.visibility = View.VISIBLE
                }
            } else {
                textviewPostItemText.visibility = View.GONE
                imageviewPostItem.visibility = View.VISIBLE
                if (post.preview?.images?.get(0)?.source?.url != null) {
                    val imageUrl =
                        post.preview.images[0].source!!.url?.replace(
                            removePart, ""
                        )
                    Glide.with(itemView)
                        .load(imageUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageviewPostItem)
                }
            }


            textviewPostAuthor.text = "By ${post.author}"
            textviewPostItemTitle.text = post.title
            textviewPostItemScore.text = getShortenedValue(post.score)
            textviewPostItemCommentCount.text = getShortenedValue(post.num_comments)

            val epoch = post.created_utc
            if (epoch != null) {
                textviewPostItemAge.text = calculateAgeDifferenceLocalDateTime(epoch, 1)
            }


            if(post.saved == true) {
                imageSaved.visibility = View.VISIBLE
            }else {
                imageSaved.visibility = View.INVISIBLE
            }


            when (post.likes) {
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
        fun onMoreClick(post: RedditChildrenData, type: Int)
    }


    companion object {
        private const val removePart = "amp;"
        private const val upvoteColor = "#E24824"
        private const val downvoteColor = "#5250DE"
    }

}