package com.example.snapshotsforreddit.ui.tabs.downloadedposts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.data.room.Post

import com.example.snapshotsforreddit.databinding.DownloadedPostItemBinding


class DownloadedPostsAdapter(private val listener: OnItemClickListener) : ListAdapter<Post, DownloadedPostsAdapter.DownloadedPostsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadedPostsViewHolder {
        //need parent.context for reference to activity/fragment to get layout inflater to inflate the binding layout
        val binding = DownloadedPostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //instantiate viewholder and return it
        return DownloadedPostsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DownloadedPostsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    //instead of using find view by id for everything, we can use viewbinding
    inner class DownloadedPostsViewHolder(private val binding: DownloadedPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //executes when viewholder is instantiated
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    //an item that is deleted but still shows in screen due to animations/etc has a positon of -1 or NO_POSITION
                    if(position != RecyclerView.NO_POSITION) {
                        val post = getItem(position)
                        listener.onItemClick(post)
                    }
                }
            }
        }


        //put the data into the views in the layout
        //without using databinding
        fun bind(post: Post) {
            //instead of writing binding. for each view, we can just use apply
            binding.apply {
                postItemTitle.text = post.title
            }
        }

    }


    interface OnItemClickListener {
        fun onItemClick(post: Post)

    }

    class DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            //if contents are the same but are at different positions, this method will know how to move that item
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }



}