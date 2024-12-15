package com.example.dicodingevents.ui.search

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevents.data.remote.response.ListEventsItem
import com.example.dicodingevents.databinding.FragmentSearchBinding
import com.example.dicodingevents.ui.adapter.VerticalEventAdapter
import com.example.dicodingevents.ui.viewmodel.EventViewModel
import com.example.dicodingevents.ui.viewmodel.ViewModelFactory

class SearchFragment : Fragment() {
    private val searchViewModel by viewModels<EventViewModel>{
        ViewModelFactory.getInstance(requireActivity())
    }
    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        _binding=null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setEventData(data: List<ListEventsItem>) {
        if (data.isEmpty()) {
            Toast.makeText(requireContext(), "No events found", Toast.LENGTH_SHORT).show()
        } else {
            val adapter = VerticalEventAdapter()
            adapter.submitList(data)
            binding.rvSearch.adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvSearch.layoutManager = layoutManager

        searchViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        searchViewModel.searchResults.observe(viewLifecycleOwner) {
            setEventData(it)
        }
        if (!isNetworkAvailable()) {
            Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchViewModel.searchEvent(it)
                    showLoading(true)  // Show loading while searching
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally trigger search on text change for instant search
                newText?.let {
                    searchViewModel.searchEvent(it)
                    showLoading(true)  // Show loading while searching
                }
                return true
            }
        })

    }
    @Suppress("DEPRECATION")
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}