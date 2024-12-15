package com.example.dicodingevents.ui.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevents.data.local.entity.EventEntity
import com.example.dicodingevents.databinding.ItemVertikalEventBinding
import com.example.dicodingevents.ui.DetailActivity
import com.example.dicodingevents.ui.DetailActivity.Companion.EXTRA_ID

class FavoriteEventAdapter : ListAdapter<EventEntity, FavoriteEventAdapter.EventViewHolder>(diffCallback) {
    class EventViewHolder(private val binding: ItemVertikalEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (event: EventEntity) {
            binding.title.text = event.name
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .into(binding.image)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_ID, event.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(
                oldItem: EventEntity,
                newItem: EventEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: EventEntity,
                newItem: EventEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemVertikalEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }
}