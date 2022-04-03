package com.example.snapshotsforreddit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.databinding.SubscribedItemBinding
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenObject

class SubscribedAdapter(private val clickListener: OnItemClickListener) :
    PagingDataAdapter<SubscribedChildrenObject, SubscribedAdapter.SubscribedViewHolder>(
        SUBREDDIT_COMPARATOR
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubscribedViewHolder {
        val binding = SubscribedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SubscribedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscribedViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    inner class SubscribedViewHolder(private val binding: SubscribedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        clickListener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(subredditObject: SubscribedChildrenObject) {
            val removePart = "amp;"
            //TODO FIX CODE HERE : REFORMAT STATEMENTS
            binding.apply {
                val currIconUrl = subredditObject.data!!.community_icon
                val iconUrl: String = if (subredditObject.data.community_icon == null && subredditObject.data.icon_img == null) {
                    ""
                }else if(currIconUrl  == "") {
                    subredditObject.data.icon_img!!.replace(removePart,"")
                }else {
                    currIconUrl!!.replace(removePart,"")
                }
                Glide.with(itemView)
                    .load(iconUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageSubredditItem)
                println(iconUrl )
                println(subredditObject.data!!.display_name_prefixed)
                textviewSubredditItemTitle.text = subredditObject.data!!.display_name_prefixed
                favoriteSubredditItem.isVisible = subredditObject.data.user_has_favorited!!
            }
        }
    }



    companion object {
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