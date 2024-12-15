package com.example.dicodingevents.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "favorite_table")
@Parcelize
data class EventEntity(
    @PrimaryKey
    val id : Int,

    @field:ColumnInfo(name = "name")
    val name : String,
    @field:ColumnInfo(name = "mediaCover")
    val mediaCover : String,

) : Parcelable

