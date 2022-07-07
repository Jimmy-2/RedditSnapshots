package com.jimmywu.snapshotsforreddit.ui.navigationbartabs.home

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jimmywu.snapshotsforreddit.R
import com.jimmywu.snapshotsforreddit.data.room.cache.subscribedsubreddit.SubscribedSubreddit
import com.jimmywu.snapshotsforreddit.databinding.ItemSubscribedBinding

class SubscribedSubredditViewHolder(val binding: ItemSubscribedBinding, private val onSubscribedClick: (Int) -> Unit ) :
    RecyclerView.ViewHolder(binding.root)  {
    
    init{
        binding.apply {
            root.setOnClickListener{
                val position = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    onSubscribedClick(position)
                }
            }
        }
    }

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
                .error(R.drawable.ic_temp_r)
                .into(imageSubredditIcon)


//            if(subreddit.primary_color != null && subreddit.primary_color != "") {
//                imageSubredditIcon.setBackgroundColor(Color.parseColor(subreddit.primary_color))
//            }

//            textviewSubredditItemTitle.text = subreddit.display_name_prefixed
            textviewSubredditItemTitle.text = subreddit.display_name
            favoriteSubredditItem.isVisible = subreddit.user_has_favorited!!

        }
    }



}