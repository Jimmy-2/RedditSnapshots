package com.example.snapshotsforreddit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.data.room.Post
import com.example.snapshotsforreddit.databinding.DownloadedPostItemBinding


class DownloadedPostsAdapter (private val onItemClicked: (Post) -> Unit) :
    ListAdapter<Post, DownloadedPostsAdapter.PostViewHolder>(DiffCallback) {

    class PostViewHolder (private var binding: DownloadedPostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Post) {
            //binding.postitemTitle.text = item.title
            binding.downloadedPostItem = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            DownloadedPostItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.permalink == newItem.permalink
            }
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }





}
