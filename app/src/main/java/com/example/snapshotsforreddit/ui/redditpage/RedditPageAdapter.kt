package com.example.snapshotsforreddit.ui.redditpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.RedditPagePostItemBinding

import com.example.snapshotsforreddit.network.responses.RedditPageChildrenObject
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenObject
import com.example.snapshotsforreddit.ui.home.SubscribedAdapter

class RedditPageAdapter() :
    PagingDataAdapter<RedditPageChildrenObject, RedditPageAdapter.RedditPageViewHolder>(
        POST_COMPARATOR
    ) {
    //TODO: Instead of 1 item layout for a post, have 2 different layouts, 1 for text only post vs image/video/gif post


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditPageViewHolder {
        val binding = RedditPagePostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RedditPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RedditPageViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }


    inner class RedditPageViewHolder(private val binding: RedditPagePostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postObject: RedditPageChildrenObject) {
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
                        val iconUrl =
                            currentPost.preview.images[0].source!!.url?.replace(removePart, "")
                        Glide.with(itemView)
                            .load(iconUrl)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .error(R.drawable.ic_error)
                            .into(imageviewPostItem)
                    }


                }

                textviewPostItemSubreddit.text = currentPost.subreddit
                textviewPostItemTitle.text = currentPost.title
                textviewPostItemText.text = currentPost.selftext
                textviewPostItemScore.text = currentPost.score.toString()
                textviewPostItemCommentCount.text = currentPost.num_comments.toString()
            }
        }

    }

    companion object {
        private val POST_COMPARATOR =
            object : DiffUtil.ItemCallback<RedditPageChildrenObject>() {
                override fun areItemsTheSame(
                    oldItem: RedditPageChildrenObject,
                    newItem: RedditPageChildrenObject
                ): Boolean {

                    //CHANGE THIS TO data.name LATER SO WE DO NOT HAVE TO UPDATE EVEN IF SUBSCRIPTION COUNT CHANGES
                    return oldItem.data == newItem.data
                }

                override fun areContentsTheSame(
                    oldItem: RedditPageChildrenObject,
                    newItem: RedditPageChildrenObject
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }




}