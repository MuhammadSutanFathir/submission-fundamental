package com.example.dicodingevents.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.dicodingevents.data.EventRepository
import com.example.dicodingevents.data.local.entity.EventEntity
import com.example.dicodingevents.data.remote.response.ListEventsItem
import com.example.dicodingevents.data.remote.retrofit.ApiConfig
import com.example.dicodingevents.ui.SettingPreferences
import kotlinx.coroutines.launch

class EventViewModel(private val pref: SettingPreferences, private val eventRepository: EventRepository) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> = pref.getThemeSetting().asLiveData()

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch { pref.saveThemeSetting(isDarkModeActive) }
    }

    private val _tvHeading = MutableLiveData("Dicoding Event")
    val tvHeading: LiveData<String> = _tvHeading

    private val _tvHeadingDesc = MutableLiveData("Recommendation event for you!")
    val tvHeadingDesc: LiveData<String> = _tvHeadingDesc

    private val _tvHomeUpcoming = MutableLiveData("Upcoming Event")
    val tvHomeUpcoming: LiveData<String> = _tvHomeUpcoming

    private val _tvHomeFinished = MutableLiveData("Finished Event!")
    val tvHomeFinished: LiveData<String> = _tvHomeFinished

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listEventsActive = MutableLiveData<List<ListEventsItem>>()
    val listEventsActive: LiveData<List<ListEventsItem>> = _listEventsActive

    private val _searchResults = MutableLiveData<List<ListEventsItem>>()
    val searchResults: LiveData<List<ListEventsItem>> = _searchResults

    private val _listEventsFinished = MutableLiveData<List<ListEventsItem>>()
    val listEventsFinished: LiveData<List<ListEventsItem>> = _listEventsFinished

    private val _event = MutableLiveData<ListEventsItem>()
    val event: LiveData<ListEventsItem> = _event

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    init {
        findEventActive()
        findEventFinished()
    }

    private fun findEventFinished() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getEvent(0)
                _listEventsFinished.value = response.listEvents
            } catch (e: Exception) {
                Log.d("EventViewModel", "findEventFinished: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun findEventActive() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getEvent(1)
                _listEventsActive.value = response.listEvents
            } catch (e: Exception) {
                Log.d("EventViewModel", "findEventActive: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchEvent(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getEventByKeyword(-1, query)
                _searchResults.value = response.listEvents
            } catch (e: Exception) {
                Log.d("EventViewModel", "searchEvent: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getItemById(eventId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getEventById(eventId)
                _event.value = response.event
            } catch (e: Exception) {
                Log.d("EventViewModel", "getItemById: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun insertFavoriteEvent(event: EventEntity) {
        viewModelScope.launch {
            try {
                eventRepository.insertFavoriteEvent(event)
                _isFavorite.value = true
            } catch (e: Exception) {
                Log.d("EventViewModel", "insertFavoriteEvent: ${e.message}")
            }
        }
    }

    fun deleteFavoriteEvent(id: Int) {
        viewModelScope.launch {
            try {
                eventRepository.deleteEvent(id)
                _isFavorite.value = false
            } catch (e: Exception) {
                Log.d("EventViewModel", "deleteFavoriteEvent: ${e.message}")
            }
        }
    }

    fun getAllFavoriteEvents() = eventRepository.getAllFavoriteEvents()

    fun checkIfFavorite(eventId: Int) {
        viewModelScope.launch {
            try {
                val event = eventRepository.getEventById(eventId)
                _isFavorite.value = event != null
            } catch (e: Exception) {
                Log.d("EventViewModel", "checkIfFavorite: ${e.message}")
            }
        }
    }

}
