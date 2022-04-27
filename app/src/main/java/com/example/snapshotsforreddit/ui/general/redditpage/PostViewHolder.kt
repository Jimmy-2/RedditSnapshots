package com.example.snapshotsforreddit.ui.general.redditpage

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.PostItemBinding
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import com.example.snapshotsforreddit.util.calculateAgeDifferenceLocalDateTime
import com.example.snapshotsforreddit.util.getShortenedValue

class PostViewHolder(
    private val adapter: PagingDataAdapter<RedditChildrenObject, RecyclerView.ViewHolder>,
    private val onClickListener: OnItemClickListener,
    private val binding: PostItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    private var post: RedditChildrenObject? = null

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
                    if (post!!.data?.likes == true) {
                        post!!.data?.likes = null
                        onClickListener.onVoteClick(post!!, 0)
                    } else {
                        post!!.data?.likes = true
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
                    if (post!!.data?.likes == false) {
                        post!!.data?.likes = null
                        onClickListener.onVoteClick(post!!, 0,)
                    } else {
                        post!!.data?.likes = false
                        onClickListener.onVoteClick(post!!, -1,)
                    }
                    adapter.notifyItemChanged(position)
                }
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    fun bind(postObject: RedditChildrenObject) {
        this.post = postObject
        val currentPost = postObject.data
        //TODO FIX CODE HERE : REFORMAT STATEMENTS
        binding.apply {
            if (currentPost != null) {
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

                when (post?.data?.is_self) {
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
                    null -> {
                        textviewPostItemScore.setTextColor(coil.base.R.color.androidx_core_secondary_text_default_material_light)
                        imageArrow.setImageResource(R.drawable.ic_up_arrow_null)
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(post: RedditChildrenObject)
        fun onVoteClick(post: RedditChildrenObject, type: Int)

    }


    companion object {
        private const val removePart = "amp;"
        private const val upvoteColor = "#E24824"
        private const val downvoteColor = "#5250DE"
    }

}