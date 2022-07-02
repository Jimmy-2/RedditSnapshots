package com.jimmywu.snapshotsforreddit.ui.common.loadstate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jimmywu.snapshotsforreddit.R
import com.jimmywu.snapshotsforreddit.databinding.ItemLoadStateBinding

class RedditLoadStateAdapter(private val retry: () -> Unit): LoadStateAdapter<RedditLoadStateAdapter.LoadStateItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateItemViewHolder {
        val binding = ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateItemViewHolder(binding)
    }

    //loadstate tells us the progress of loading the current item.
    //for example: if loading: display progress bar, if error: show retry
    override fun onBindViewHolder(holder: LoadStateItemViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateItemViewHolder(private val binding: ItemLoadStateBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.buttonLoadingRetry.setOnClickListener {
                retry.invoke()
            }
        }


        fun bind(loadState: LoadState) {
            binding.apply {
                progressbarLoading.isVisible = loadState is LoadState.Loading
                buttonLoadingRetry.isVisible = loadState !is LoadState.Loading
                textviewLoadingError.isVisible = loadState !is LoadState.Loading

                if (loadState is LoadState.Error) {
                    textviewLoadingError.text = loadState.error.localizedMessage
                        ?: binding.root.context.getString(R.string.error_with_loading)
                }
            }
        }
    }


}