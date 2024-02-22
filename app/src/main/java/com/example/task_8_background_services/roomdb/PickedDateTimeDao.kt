package com.example.task_8_background_services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface PickedDateTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedDateTime(dateTime: PickedDateTime)
    @Query("SELECT * FROM picked_date_time")
    suspend fun getAllEntries(): List<PickedDateTime>
}