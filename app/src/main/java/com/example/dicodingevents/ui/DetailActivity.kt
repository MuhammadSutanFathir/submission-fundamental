package com.example.dicodingevents.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.dicodingevents.R
import com.example.dicodingevents.data.local.entity.EventEntity
import com.example.dicodingevents.data.remote.response.ListEventsItem
import com.example.dicodingevents.databinding.ActivityDetailBinding
import com.example.dicodingevents.ui.viewmodel.EventViewModel
import com.example.dicodingevents.ui.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<EventViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private var isFavorite = false


    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val controller = window.insetsController
        controller?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )


        val id = intent.getIntExtra(EXTRA_ID, 0)
        detailViewModel.getItemById(id)

        window.statusBarColor = Color.TRANSPARENT
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }


        detailViewModel.event.observe(this) {
            event ->
            Glide.with(this@DetailActivity)
                .load(event.imageLogo)
                .into(binding.imageDetail)

            binding.titleDetail.text = event.name
            binding.summary.text = event.summary
            "Diselenggarakan oleh: \n${event.ownerName}".also { binding.ownerDetail.text = it }
            "Mulai: ${event.beginTime}".also { binding.beginTimeDetail.text = it }
            "Selesai: ${event.endTime}".also { binding.endTimeDetail.text = it }
            binding.quotaDetail.text = String.format("Sisa Quota: %s Peserta", event.quota - event.registrants)

            binding.descriptionDetail.text = HtmlCompat.fromHtml(
                event.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            binding.buttonDetail.setOnClickListener {
                val link = event.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
            detailViewModel.checkIfFavorite(id)
            detailViewModel.isFavorite.observe(this) { isFavorite ->
                this.isFavorite = isFavorite
                updateFavoriteIcon(isFavorite)
            }
            binding.favoriteButton.setOnClickListener {
                toggleFavorite(event)
            }

        }


    }
    private fun toggleFavorite(event: ListEventsItem) {
        val eventEntity = EventEntity(
            id = event.id,
            name = event.name,
            mediaCover = event.mediaCover
        )

        if (isFavorite) {
            detailViewModel.deleteFavoriteEvent(event.id)
            Log.d("DetailActivity", "Terhapus")
        } else {
            detailViewModel.insertFavoriteEvent(eventEntity)
        }
    }
    private fun updateFavoriteIcon(isFavorite: Boolean) {
        binding.favoriteButton.setImageResource(
            if (isFavorite) R.drawable.baseline_favorite_24
            else R.drawable.baseline_favorite_border_24
        )
    }
}