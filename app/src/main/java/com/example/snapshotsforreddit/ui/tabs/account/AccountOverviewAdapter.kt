package com.example.snapshotsforreddit.ui.tabs.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.*
import com.example.snapshotsforreddit.network.responses.RedditChildrenObject
import retrofit2.http.POST

class AccountOverviewAdapter() :
    PagingDataAdapter<RedditChildrenObject, AccountOverviewAdapter.AccountOverviewViewHolder>(
        POST_COMPARATOR
    ) {
    //TODO: Instead of 1 item layout for a post, have 2 different layouts, 1 for text only post vs image/video/gif post

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountOverviewViewHolder {
        return when (viewType) {
            COMMENT -> AccountOverviewViewHolder.CommentViewHolder(AccountCommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            POST -> AccountOverviewViewHolder.PostViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            USER_DATA -> AccountOverviewViewHolder.UserDataViewHolder(AccountUserDataItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            DEFAULT -> AccountOverviewViewHolder.DefaultViewHolder(AccountDefaultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            DEFAULT_TOP -> AccountOverviewViewHolder.DefaultTopViewHolder(AccountDefaultTopItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            DEFAULT_BOTTOM ->AccountOverviewViewHolder.DefaultBottomViewHolder(AccountDefaultBottomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Error with view type")
        }
    }

    override fun onBindViewHolder(holder: AccountOverviewViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            when (holder) {
                is AccountOverviewViewHolder.CommentViewHolder -> holder.bind(currentItem)
                is AccountOverviewViewHolder.PostViewHolder -> holder.bind(currentItem)
                is AccountOverviewViewHolder.UserDataViewHolder -> holder.bind(currentItem)
                is AccountOverviewViewHolder.DefaultViewHolder -> holder.bind(currentItem)
                is AccountOverviewViewHolder.DefaultTopViewHolder -> holder.bind(currentItem)
                is AccountOverviewViewHolder.DefaultBottomViewHolder -> holder.bind(currentItem)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.kind) {
            "t1" -> COMMENT
            "t3" -> POST
            "userData" -> USER_DATA
            "default" -> DEFAULT
            "defaultTop" -> DEFAULT_TOP
            "defaultBottom" -> DEFAULT_BOTTOM
            else -> ERROR
        }

    }


    sealed class AccountOverviewViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        class CommentViewHolder(val binding: AccountCommentItemBinding) : AccountOverviewViewHolder(binding) {
            fun bind(postObject: RedditChildrenObject) {
                val currentPost = postObject.data
                //TODO FIX CODE HERE : REFORMAT STATEMENTS
                binding.apply {
                    //if true, is text only post
                    textviewAccountCommentBody.text = currentPost?.body
                    textviewAccountCommentPostTitle.text = currentPost?.link_title
                    textviewAccountCommentPostSubreddit.text = currentPost?.subreddit

                }

            }

        }

        class PostViewHolder(val binding: PostItemBinding) : AccountOverviewViewHolder(binding) {
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




        class UserDataViewHolder(val binding: AccountUserDataItemBinding): AccountOverviewViewHolder(binding) {
            fun bind(postObject: RedditChildrenObject) {
                val currentPost = postObject.data
                binding.apply {
                    if (currentPost != null) {
                        buttonCommentKarma.text = currentPost.userData?.comment_karma.toString()
                        buttonPostKarma.text = currentPost.userData?.link_karma.toString()
                    }

                }
            }
        }

        class DefaultViewHolder(val binding: AccountDefaultItemBinding): AccountOverviewViewHolder(binding) {
            fun bind(postObject: RedditChildrenObject) {
                val currentPost = postObject.data
                binding.apply {
                    if (currentPost != null) {
                        textviewDefault.text = currentPost.body
                    }
                }
            }
        }
        class DefaultTopViewHolder(val binding: AccountDefaultTopItemBinding): AccountOverviewViewHolder(binding) {
            fun bind(postObject: RedditChildrenObject) {
                val currentPost = postObject.data
                binding.apply {
                    if (currentPost != null) {
                        println("HELLLLLLLLLLLLLO ${currentPost.body}")
                        textviewDefault.text = currentPost.body
                    }
                }
            }
        }

        class DefaultBottomViewHolder(val binding: AccountDefaultBottomItemBinding): AccountOverviewViewHolder(binding) {
            fun bind(postObject: RedditChildrenObject) {
                val currentPost = postObject.data
                binding.apply {
                    if (currentPost != null) {
                        textviewDefault.text = currentPost.body

                    }
                }
            }
        }


    }



    companion object {
        // View Types
        private val COMMENT = 0
        private val POST = 1
        private val USER_DATA = 2
        private val DEFAULT = 3
        private val DEFAULT_TOP = 4
        private val DEFAULT_BOTTOM = 5
        private val LOADING = 6
        private val ERROR = 7



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




}