package com.example.snapshotsforreddit.ui.tabs.home

import android.graphics.Color
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.snapshotsforreddit.R
import com.example.snapshotsforreddit.data.room.cache.SubscribedSubreddit
import com.example.snapshotsforreddit.databinding.ItemSubscribedBinding

class SubscribedSubredditViewHolder(val binding: ItemSubscribedBinding) :
    RecyclerView.ViewHolder(binding.root)  {
//    init {
//        binding.root.setOnClickListener {
//            val position = bindingAdapterPosition
//            if (position != RecyclerView.NO_POSITION) {
//                val item = getItem(position)
//                if (item != null) {
//                    onClickListener.onSubredditClick(item)
//                }
//            }
//        }
//    }

    fun bind(subreddit: SubscribedSubreddit) {
        val removePart = "amp;"
        binding.apply {
            val iconUrl: String =
                if (subreddit.community_icon != "" && subreddit.community_icon != null) {
                    subreddit.community_icon.replace(removePart, "")
                } else if (subreddit.icon_img != "" && subreddit.icon_img != null) {
                    subreddit.icon_img.replace(removePart, "")
                } else {
                    ""
                }
            Glide.with(itemView)
                .load(iconUrl)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_blank)
                .into(imageSubredditIcon)


            if(subreddit.primary_color != null && subreddit.primary_color != "") {
                imageSubredditIcon.setBackgroundColor(Color.parseColor(subreddit.primary_color))
            }

//            textviewSubredditItemTitle.text = subreddit.display_name_prefixed
            textviewSubredditItemTitle.text = subreddit.display_name
            favoriteSubredditItem.isVisible = subreddit.user_has_favorited!!

        }
    }
}