package com.mrh.storyapp.data.stories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mrh.storyapp.R

class ListStoryAdapter(private val listStory: List<String>) : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_stories, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount() = listStory.size

    override fun onBindViewHolder(viewHolder: ListViewHolder, position: Int) {
        viewHolder.owner.text = listStory[position]
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val owner: TextView = itemView.findViewById(R.id.tv_story_owner)
        val imageUrl: ImageView = itemView.findViewById(R.id.img_stories)
    }
}