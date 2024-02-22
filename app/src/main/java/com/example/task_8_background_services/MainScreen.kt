package com.example.task_8_background_services

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.task_8_background_services.databinding.ActivityMainScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.util.Locale

class MainScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var pickedDateTimeDao: PickedDateTimeDao
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializingBinding()
        initializingVariables()
        initializingRoomDBAndDao()
        clickListeners()

    }

    private fun initializingRoomDBAndDao() {
        db = AppDatabase.getInstance(this)
        pickedDateTimeDao = db.selectedDateTimeDao()
    }


    private fun initializingVariables() {
    }

    private fun clickListeners() {
        binding.addButton.setOnClickListener(View.OnClickListener {
            showDateTimePicker()
        })

    }

    private fun showDateTimePicker() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                showTimePicker(selectedDate)
            },
            year, month, day
        )

        datePickerDialog.show()

    }

    private fun showTimePicker(selectedDate: Calendar) {
        val hour = selectedDate.get(Calendar.HOUR_OF_DAY)
        val minute = selectedDate.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                selectedDate.set(Calendar.HOUR_OF_DAY, selectedHour)
                selectedDate.set(Calendar.MINUTE, selectedMinute)

                val currentTime = Calendar.getInstance()

                val timeDifference = selectedDate.timeInMillis - currentTime.timeInMillis
                val tenMinutesInMillis = 10 * 60 * 1000 // 10 minutes in milliseconds

                if (timeDifference >= tenMinutesInMillis) {

                    val simpleDateFormat =
                        SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
                    val formattedDateTime = simpleDateFormat.format(selectedDate.time)
                    Toast.makeText(
                        this,
                        "Selected Date and Time: $formattedDateTime",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Please select a time at least 10 minutes ahead.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            hour, minute, false
        )

        timePickerDialog.show()
    }

    private fun initializingBinding() {
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun saveDateTimeToDatabase(selectedDateTime: PickedDateTime) {
        GlobalScope.launch(Dispatchers.IO) {
            pickedDateTimeDao.insertSelectedDateTime(selectedDateTime)
            val allEntries = pickedDateTimeDao.getAllEntries()
            allEntries.forEach {
                Log.d("RoomDB", "ID: ${it.id}, DateTimeInMillis: ${it.dateTime}")
            }
        }
    }
}