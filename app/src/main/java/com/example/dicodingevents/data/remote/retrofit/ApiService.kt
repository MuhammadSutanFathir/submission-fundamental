package com.example.dicodingevents.data.remote.retrofit

import com.example.dicodingevents.data.remote.response.DetailEvent
import com.example.dicodingevents.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("events")

    suspend fun getEvent(
        @Query("active") active: Int
    ) : EventResponse

    @GET("events/{id}")

    suspend fun getEventById(
        @Path("id") id : Int
    ): DetailEvent

    @GET("events")
    suspend fun getEventByKeyword(
        @Query("active") active: Int,
        @Query("q") q : String
    ): EventResponse

}