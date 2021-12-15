package com.example.datetimedemo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bobgenix.datetimedialog.OnDateTimeSelectedListener
import com.bobgenix.datetimedialog.UnifiedDateTimePicker
import com.bobgenix.datetimedialog.UnifiedDateTimePickerHelper
import com.example.datetimedemo.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val DATE_FORMAT_Z = "yyyy-MM-dd HH:mm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMy.setOnClickListener {
            UnifiedDateTimePicker.Builder(this)
                .setTitle("Select Date and Time")
                .addListener(object : OnDateTimeSelectedListener {
                    override fun onDateTimeSelected(millis: Long) {
                        val sdf = SimpleDateFormat(DATE_FORMAT_Z, Locale.ROOT)
                        val calendar: Calendar = Calendar.getInstance()
                        calendar.timeInMillis = millis

                        binding.textDateTime.text = sdf.format(calendar.time)
                    }

                    override fun onPickerDismissed(millis: Long) {
                        /* no use as of now */
                    }
                })
                .show();
        }
    }
}