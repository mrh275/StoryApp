package com.mrh.storyapp.data.stories

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.mrh.storyapp.R

class ListStoryAdapter(private val activity: Activity) : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    class ListViewHolder(view: View) : ViewHolder(view) {
        val owner: TextView = view.findViewById(R.id.tv_item_name)
        val imageUrl: ImageView = view.findViewById(R.id.iv_item_photo)
        val createdAt: TextView = view.findViewById(R.id.tv_created_at)
        val cvStory: CardView = view.findViewById(R.id.cv_stories)
    }

    interface OnItemClickCallback {
        fun onItemClicked(listStoryItem: ListStoryItem)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_stories, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.owner.text = liststory?.get(position)?.name
        holder.createdAt.text = liststory?.get(position)?.createdAt
        Glide.with(holder.imageUrl)
            .load(liststory?.get(position)?.photoUrl)
            .into(holder.imageUrl)

        holder.cvStory.startAnimation(AnimationUtils.loadAnimation(holder.cvStory.context, R.anim.grow_appear))
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
