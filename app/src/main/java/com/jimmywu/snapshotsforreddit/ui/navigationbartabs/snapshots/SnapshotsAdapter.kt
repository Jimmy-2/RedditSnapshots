package com.jimmywu.snapshotsforreddit.ui.navigationbartabs.snapshots

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jimmywu.snapshotsforreddit.data.room.snapshots.Snapshot
import com.jimmywu.snapshotsforreddit.databinding.ItemSnapshotBinding


class SnapshotsAdapter(private val onClickListener: OnItemClickListener) : ListAdapter<Snapshot, SnapshotsAdapter.SnapshotsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnapshotsViewHolder {
        //need parent.context for reference to activity/fragment to get layout inflater to inflate the binding layout
        val binding = ItemSnapshotBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //instantiate viewholder and return it
        return SnapshotsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SnapshotsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    //instead of using find view by id for everything, we can use viewbinding
    inner class SnapshotsViewHolder(private val binding: ItemSnapshotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //executes when viewholder is instantiated
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    //an item that is deleted but still shows in screen due to animations/etc has a positon of -1 or NO_POSITION
                    if(position != RecyclerView.NO_POSITION) {
                        val snapshot = getItem(position)
                        onClickListener.onItemClick(snapshot)
                    }
                }
            }
        }


        //put the data into the views in the layout
        //without using databinding
        fun bind(snapshot: Snapshot) {
            //instead of writing binding. for each view, we can just use apply
            binding.apply {
                postItemTitle.text = snapshot.title
            }
        }

    }


    interface OnItemClickListener {
        fun onItemClick(snapshot: Snapshot)

    }

    class DiffCallback : DiffUtil.ItemCallback<Snapshot>() {
        override fun areItemsTheSame(oldItem: Snapshot, newItem: Snapshot): Boolean {
            //if contents are the same but are at different positions, this method will know how to move that item
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Snapshot, newItem: Snapshot): Boolean {
            return oldItem == newItem
        }

    }



}