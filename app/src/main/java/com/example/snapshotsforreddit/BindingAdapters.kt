package com.example.snapshotsforreddit

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.snapshotsforreddit.adapter.FrontPageAdapter
import com.example.snapshotsforreddit.network.responses.ChildrenData

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ChildrenData>?) {
    val adapter = recyclerView.adapter as FrontPageAdapter
    adapter.submitList(data)
}


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.ic_downloading)
            error(R.drawable.ic_error)
        }
    }
}
