package com.example.snapshotsforreddit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.snapshotsforreddit.databinding.SubscribedSubredditsItemBinding


import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenData
import com.example.snapshotsforreddit.network.responses.subscribed.SubscribedChildrenObject


class SubscribedSubredditsAdapter (private val listener: OnItemClickListener) : ListAdapter<SubscribedChildrenObject, SubscribedSubredditsAdapter.SubscribedSubredditsViewHolder>(DiffCallback())  {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubscribedSubredditsViewHolder {
        val binding = SubscribedSubredditsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //instantiate viewholder and return it
        return SubscribedSubredditsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscribedSubredditsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<SubscribedChildrenObject>() {
        override fun areItemsTheSame(oldItem: SubscribedChildrenObject, newItem: SubscribedChildrenObject): Boolean {
            //if contents are the same but are at different positions, this method will know how to move that item
            return oldItem.data == newItem.data
        }

        override fun areContentsTheSame(oldItem: SubscribedChildrenObject, newItem: SubscribedChildrenObject): Boolean {
            return oldItem == newItem
        }

    }

    inner class SubscribedSubredditsViewHolder(private val binding: SubscribedSubredditsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //executes when viewholder is instantiated
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    //an item that is deleted but still shows in screen due to animations/etc has a positon of -1 or NO_POSITION
                    if(position != RecyclerView.NO_POSITION) {
                        val subreddit = getItem(position)
                        listener.onItemClick(subreddit)
                    }
                }
            }
        }


        //put the data into the views in the layout
        //without using databinding
        fun bind(subreddit: SubscribedChildrenObject) {
            //instead of writing binding. for each view, we can just use apply
            var counter = 0;
            counter++
            println(counter)
            binding.apply {
                titleSubredditItem.text = subreddit.data!!.display_name_prefixed
                favoriteSubredditItem.isVisible = subreddit.data.user_has_favorited!!
            }
        }

    }
    interface OnItemClickListener {
        fun onItemClick(subreddit: SubscribedChildrenObject)

    }


}