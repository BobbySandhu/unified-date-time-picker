package com.example.datetimedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.bobgenix.datetimedialog.AndroidUtilities
import com.bobgenix.datetimedialog.BottomSheet
import com.bobgenix.datetimedialog.DialogHelper
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

        AndroidUtilities.checkDisplaySize(this, resources.configuration)
        binding.buttonMy.setOnClickListener {
            Log.d("aaaa", "clicked.....")
            DialogHelper.createDatePickerDialog(
                this,
                -1,
                object : DialogHelper.ScheduleDatePickerDelegate {
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