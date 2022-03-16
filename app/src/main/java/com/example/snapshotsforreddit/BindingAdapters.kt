package com.example.snapshotsforreddit

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshotsforreddit.adapter.FrontPageAdapter
import com.example.snapshotsforreddit.network.responses.ChildrenData

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ChildrenData>?) {
    val adapter = recyclerView.adapter as FrontPageAdapter
    adapter.submitList(data)
}


