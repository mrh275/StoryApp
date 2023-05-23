package com.mrh.storyapp.ui.story

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.mrh.storyapp.R
import com.mrh.storyapp.data.stories.ListStoryItem
import com.mrh.storyapp.databinding.ItemRowStoriesBinding
import com.mrh.storyapp.ui.story.detail.DetailStoryActivity

class ListStoryAdapter : PagingDataAdapter<ListStoryItem, ListStoryAdapter.ListViewHolder>(
    DIFF_CALLBACK
) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    class ListViewHolder(private val binding: ItemRowStoriesBinding) : ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            binding.tvItemName.text = data.name
            Glide.with(binding.ivItemPhoto)
                .load(data.photoUrl)
                .into(binding.ivItemPhoto)
            binding.tvCreatedAt.text = data.createdAt
            binding.cvStories.startAnimation(AnimationUtils.loadAnimation(binding.cvStories.context, R.anim.grow_appear))
            binding.root.setOnClickListener {
                val intent = Intent(it.context, DetailStoryActivity::class.java)
                    .also { detail ->
                        detail.putExtra(DetailStoryActivity.EXTRA_ID, data.id)
                        detail.putExtra(DetailStoryActivity.EXTRA_NAME, data.name)
                        detail.putExtra(DetailStoryActivity.EXTRA_DESCRIPTION, data.description)
                        detail.putExtra(DetailStoryActivity.EXTRA_PHOTOURL, data.photoUrl)
                    }
                itemView.context.startActivity(intent)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(listStoryItem: ListStoryItem)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemRowStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null) {
            holder.bind(data)
        }
    }

}
