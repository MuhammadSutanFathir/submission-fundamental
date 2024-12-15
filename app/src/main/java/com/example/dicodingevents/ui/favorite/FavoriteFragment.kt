package com.example.dicodingevents.ui.favorite

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevents.databinding.FragmentFavoriteBinding
import com.example.dicodingevents.ui.adapter.FavoriteEventAdapter
import com.example.dicodingevents.ui.viewmodel.EventViewModel
import com.example.dicodingevents.ui.viewmodel.ViewModelFactory


class FavoriteFragment : Fragment() {
    private val favoriteViewModel by viewModels<EventViewModel>{
        ViewModelFactory.getInstance(requireActivity())
    }
    private var _binding: FragmentFavoriteBinding? = null

    private val binding get() = _binding!!
    private lateinit var adapter :FavoriteEventAdapter

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFavorite.layoutManager = layoutManager

        adapter = FavoriteEventAdapter() // Make sure your adapter can handle the data type
        binding.rvFavorite.adapter = adapter

        favoriteViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }


        if (!isNetworkAvailable()) {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
        }


        // Observe favorite events LiveData
        favoriteViewModel.getAllFavoriteEvents().observe(viewLifecycleOwner) { events ->
            showLoading(false)
            if (events.isNotEmpty()) {
                adapter.submitList(events)
            } else {
                Log.d("EventRepository", "Event Kosong")
                binding.rvFavorite.visibility = View.GONE
            }
        }
        showLoading(true)
        favoriteViewModel.getAllFavoriteEvents()


    }

    @Suppress("DEPRECATION")
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}