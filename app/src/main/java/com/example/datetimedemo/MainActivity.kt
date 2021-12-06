package com.example.datetimedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            Log.d("aaaa", "clicked.....")
            UnifiedDateTimePickerHelper.createDatePickerDialog(
                this,
                -1,
                object : UnifiedDateTimePickerHelper.ScheduleDatePickerDelegate {
                    override fun didSelectDate(notify: Boolean, scheduleDate: Long) {
                        val sdf = SimpleDateFormat(DATE_FORMAT_Z, Locale.ROOT)
                        val calendar: Calendar = Calendar.getInstance()
                        calendar.timeInMillis = scheduleDate.toLong()
                        Log.d("aaaa", "${sdf.format(calendar.time)}")
                    }
                }
            )
            Log.d("aaaa", "after clicked.....")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("aaaa", "back pressed...")
    }
}