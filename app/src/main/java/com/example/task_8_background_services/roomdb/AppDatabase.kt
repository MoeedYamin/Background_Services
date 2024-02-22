package com.example.task_8_background_services

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PickedDateTime::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun selectedDateTimeDao(): PickedDateTimeDao

    companion object {
        private const val DATABASE_NAME = "app_database"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                instance = db
                db
            }
        }
    }
}