package com.example.task_8_background_services

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picked_date_time")
data class PickedDateTime(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val dateTime: Long
)
