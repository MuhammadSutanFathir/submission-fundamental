package com.example.dicodingevents.data

import android.util.Log
import com.example.dicodingevents.data.local.entity.EventEntity
import com.example.dicodingevents.data.local.room.EventDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository private constructor(
    private val eventDao: EventDao,
) {

    suspend fun insertFavoriteEvent(event: EventEntity) {
        return withContext(Dispatchers.IO){
            Log.d("EventRepository", "Inserting event: ${event.name}")
            eventDao.insertEvents(event)
            Log.d("EventRepository", "Event inserted: ${event.id}")

        }
    }

    suspend fun deleteEvent(id: Int) {
        return withContext(Dispatchers.IO){
            eventDao.deleteFavoriteEvent(id)
        }
    }

    fun getAllFavoriteEvents() =
        eventDao.getAllFavoriteEvents()

    suspend fun getEventById(id: Int): EventEntity? {
        return withContext(Dispatchers.IO){
            eventDao.getFavoriteEventById(id)
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: EventRepository? = null

        fun getInstance(
            eventDao: EventDao
        ): EventRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: EventRepository(eventDao)
        }.also { INSTANCE = it }
    }
}
