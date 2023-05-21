package com.mrh.storyapp.ui.story

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
//        val owner: TextView = view.findViewById(R.id.tv_item_name)
//        val imageUrl: ImageView = view.findViewById(R.id.iv_item_photo)
//        val createdAt: TextView = view.findViewById(R.id.tv_created_at)
//        val cvStory: CardView = view.findViewById(R.id.cv_stories)
        fun bind(data: ListStoryItem) {
            binding.tvItemName.text = data.name
            Glide.with(binding.ivItemPhoto)
                .load(data.photoUrl)
                .into(binding.ivItemPhoto)
            binding.tvCreatedAt.text = data.createdAt
            binding.cvStories.startAnimation(AnimationUtils.loadAnimation(binding.cvStories.context, R.anim.grow_appear))
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(listStoryItem: ListStoryItem)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_stories, parent, false)
//        return ListViewHolder(view)
        val binding = ItemRowStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        holder.owner.text = liststory?.get(position)?.name
//        holder.createdAt.text = liststory?.get(position)?.createdAt
//        Glide.with(holder.imageUrl)
//            .load(liststory?.get(position)?.photoUrl)
//            .into(holder.imageUrl)
//
//        holder.cvStory.startAnimation(AnimationUtils.loadAnimation(holder.cvStory.context, R.anim.grow_appear))
        val data = getItem(position)
        if(data != null) {
            holder.bind(data)
        }
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(liststory?.get(holder.adapterPosition)!!)
        }
    }

    override fun getItemCount(): Int {
        return if(liststory == null) 0
        else liststory?.size!!
    }

    private var liststory: List<ListStoryItem>? = null

    fun setListStory(listStory: List<ListStoryItem>?) {
        liststory = listStory
    }
}
