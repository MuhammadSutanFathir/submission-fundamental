package com.example.dicodingevents.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevents.data.remote.response.ListEventsItem
import com.example.dicodingevents.databinding.ItemHorizontalEventBinding
import com.example.dicodingevents.ui.DetailActivity
import com.example.dicodingevents.ui.DetailActivity.Companion.EXTRA_ID

class HorizontalEventAdapter : ListAdapter<ListEventsItem, HorizontalEventAdapter.EventViewHolder>(diffCallback) {
    class EventViewHolder(private val binding: ItemHorizontalEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (event: ListEventsItem) {
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
        val diffCallback = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemHorizontalEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }
}