package com.example.snapshotsforreddit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.databinding.PostItemBinding
import com.example.snapshotsforreddit.network.responses.RedditPageChildrenData

class FrontPageAdapter(private val clickListener: FrontPageListener) : ListAdapter<RedditPageChildrenData, FrontPageAdapter.FrontPageViewHolder>(DiffCallback) {

    class FrontPageViewHolder(private var binding: PostItemBinding): RecyclerView.ViewHolder(binding.root)  {
        fun bind(clickListener: FrontPageListener, itemName: RedditPageChildrenData) {
            binding.postItem = itemName
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<RedditPageChildrenData>() {
        override fun areItemsTheSame(oldItem: RedditPageChildrenData, newItem: RedditPageChildrenData): Boolean {
            return oldItem == newItem
        }

        //check visually displayed
        override fun areContentsTheSame(oldItem: RedditPageChildrenData, newItem: RedditPageChildrenData): Boolean {
            return oldItem.toString().equals(newItem.toString())
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FrontPageViewHolder {
        return FrontPageViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    //since items do not have an id, we will have to override these functions so that recyclerview does not duplicate images when scrolling
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: FrontPageViewHolder, position: Int) {
        val postItem = getItem(position)
        holder.bind(clickListener, postItem)
    }
}
class FrontPageListener(val clickListener: (postItem: RedditPageChildrenData) -> Unit) {
    fun onclick(postItem: RedditPageChildrenData) = clickListener(postItem)
}
