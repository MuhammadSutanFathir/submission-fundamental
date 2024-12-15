package com.example.dicodingevents.ui.upcoming

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevents.data.remote.response.ListEventsItem
import com.example.dicodingevents.databinding.FragmentUpcomingBinding
import com.example.dicodingevents.ui.adapter.VerticalEventAdapter
import com.example.dicodingevents.ui.viewmodel.EventViewModel
import com.example.dicodingevents.ui.viewmodel.ViewModelFactory


class UpcomingFragment : Fragment() {
    private val upcomingViewModel by viewModels<EventViewModel>{
        ViewModelFactory.getInstance(requireActivity())
    }
    private var _binding: FragmentUpcomingBinding? = null

    private val binding get() = _binding!!

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setEventData(data: List<ListEventsItem>) {
        val adapter = VerticalEventAdapter()
        adapter.submitList(data)

        binding.rvUpcoming.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textViewHeading: TextView = binding.tvUpcoming
        val textViewHeadingDesc: TextView = binding.tvUpcomingDesc

        upcomingViewModel.tvHeading.observe(viewLifecycleOwner) {
            textViewHeading.text = it
        }

        upcomingViewModel.tvHeadingDesc.observe(viewLifecycleOwner) {
            textViewHeadingDesc.text = it
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUpcoming.layoutManager = layoutManager

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        upcomingViewModel.listEventsActive.observe(viewLifecycleOwner) {
            setEventData(it)
        }
        if (!isNetworkAvailable()) {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
        }
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