package com.example.dicodingevents.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevents.data.EventRepository
import com.example.dicodingevents.di.Injection
import com.example.dicodingevents.ui.SettingPreferences
import com.example.dicodingevents.ui.dataStore

class ViewModelFactory(
    private val preference : SettingPreferences,
    private val eventRepository: EventRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EventViewModel::class.java) -> {
                EventViewModel(preference, eventRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class data : ${modelClass.name} please create new ViewModel at factory")
        }
    }

    companion object {
        @Volatile
        private var instance : ViewModelFactory? = null

        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                val eventRepository = Injection.provideRepository(context)
                val preference = SettingPreferences.getInstance(context.dataStore)
                instance ?:ViewModelFactory(preference, eventRepository)
            }.also { instance =  it }
    }
}