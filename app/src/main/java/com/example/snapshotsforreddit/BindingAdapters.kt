package com.example.snapshotsforreddit

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.snapshotsforreddit.adapter.FrontPageAdapter
import com.example.snapshotsforreddit.adapter.DownloadedPostsAdapter
import com.example.snapshotsforreddit.database.Post
import com.example.snapshotsforreddit.network.responses.ChildrenData

@BindingAdapter("listData")
fun bindFrontPage(recyclerView: RecyclerView, data: List<ChildrenData>?) {
    val adapter = recyclerView.adapter as FrontPageAdapter
    adapter.submitList(data)
}

@BindingAdapter("SavedData")
fun bindDownloadedPosts(recyclerView: RecyclerView, data: List<Post>?) {
    val adapter = recyclerView.adapter as DownloadedPostsAdapter
    adapter.submitList(data)
}




@BindingAdapter("imageUrl", "textPost")
fun bindImage(imgView: ImageView, imageUrl: String?, textPost: Boolean?) {
    if(textPost == true) {
        imgView.setImageResource(0);
    }else {
        imageUrl?.let {
            val removePart = "amp;"
            val imgUri = imageUrl.replace(removePart,"").toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri) {
                placeholder(R.drawable.ic_downloading)
                error(R.drawable.ic_error)
            }



        }
    }

}

