package com.example.dicodingevents.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dicodingevents.data.local.entity.EventEntity


@Dao
interface EventDao {

    @Query("SELECT * FROM favorite_table")
    fun getAllFavoriteEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM favorite_table WHERE id = :id")
    fun getFavoriteEventById(id : Int): EventEntity?

    @Query("DELETE FROM favorite_table WHERE id = :id")
    suspend fun deleteFavoriteEvent(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(eventEntity : EventEntity)

    @Update
    suspend fun updateEvents(eventEntity : EventEntity)
}