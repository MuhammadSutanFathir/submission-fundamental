package com.example.dicodingevents.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevents.data.remote.response.ListEventsItem
import com.example.dicodingevents.databinding.FragmentHomeBinding
import com.example.dicodingevents.ui.adapter.HorizontalEventAdapter
import com.example.dicodingevents.ui.adapter.VerticalEventAdapter
import com.example.dicodingevents.ui.viewmodel.EventViewModel
import com.example.dicodingevents.ui.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {

    private val homeViewModel by viewModels<EventViewModel>{
        ViewModelFactory.getInstance(requireActivity())
    }
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setEventDataFinished(data: List<ListEventsItem>) {
        val adapter = VerticalEventAdapter()
        adapter.submitList(data.take(5))

        binding.rvHomeVertical.adapter = adapter
    }

    private fun setEventDataActive(data: List<ListEventsItem>) {
        val adapter = HorizontalEventAdapter()
        adapter.submitList(data.take(5))

        binding.rvHomeHorizontal.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewHeading: TextView = binding.tvHome
        val textViewHeadingDesc: TextView = binding.tvHomeDesc

        homeViewModel.tvHeading.observe(viewLifecycleOwner) {
            textViewHeading.text = it
        }

        homeViewModel.tvHeadingDesc.observe(viewLifecycleOwner) {
            textViewHeadingDesc.text = it
        }


        val textViewHomeUpcoming: TextView = binding.tvHomeUpcoming
        val textViewHomeFinished: TextView = binding.tvHomeFinished

        homeViewModel.tvHomeUpcoming.observe(viewLifecycleOwner) {
            textViewHomeUpcoming.text = it
        }

        homeViewModel.tvHomeFinished.observe(viewLifecycleOwner) {
            textViewHomeFinished.text = it
        }
        val layoutManagerHorizontal = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val layoutManagerVertical = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvHomeVertical.layoutManager = layoutManagerVertical
        binding.rvHomeHorizontal.layoutManager = layoutManagerHorizontal

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.listEventsActive.observe(viewLifecycleOwner) {
            setEventDataActive(it)
        }

        homeViewModel.listEventsFinished.observe(viewLifecycleOwner) {
            setEventDataFinished(it)
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