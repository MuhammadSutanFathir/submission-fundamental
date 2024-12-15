package com.example.dicodingevents.di

import android.content.Context
import com.example.dicodingevents.data.EventRepository
import com.example.dicodingevents.data.local.room.EventDatabase

object Injection {
    fun provideRepository(context: Context) : EventRepository {
        val db = EventDatabase.getDatabase(context)
        val dao = db.eventDao()
        return EventRepository.getInstance(dao)
    }
}