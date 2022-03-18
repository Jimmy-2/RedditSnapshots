package com.example.snapshotsforreddit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.databinding.PostItemBinding
import com.example.snapshotsforreddit.network.responses.ChildrenData

class FrontPageAdapter(val clickListener: FrontPageListener) : ListAdapter<ChildrenData, FrontPageAdapter.FrontPageViewHolder>(DiffCallback) {

    class FrontPageViewHolder(private var binding: PostItemBinding): RecyclerView.ViewHolder(binding.root)  {
        fun bind(clickListener: FrontPageListener, itemName: ChildrenData) {
            binding.postItem = itemName
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<ChildrenData>() {
        override fun areItemsTheSame(oldItem: ChildrenData, newItem: ChildrenData): Boolean {
            return oldItem.permalink == newItem.permalink
        }

        //check visually displayed
        override fun areContentsTheSame(oldItem: ChildrenData, newItem: ChildrenData): Boolean {
            return oldItem.title == newItem.title && oldItem.num_comments == newItem.num_comments && oldItem.score == newItem.score
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FrontPageAdapter.FrontPageViewHolder {
        return FrontPageViewHolder(
            PostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FrontPageAdapter.FrontPageViewHolder, position: Int) {
        val postItem = getItem(position)
        holder.bind(clickListener, postItem)
    }
}
class FrontPageListener(val clickListener: (postItem: ChildrenData) -> Unit) {
    fun onclick(postItem: ChildrenData) = clickListener(postItem)
}
