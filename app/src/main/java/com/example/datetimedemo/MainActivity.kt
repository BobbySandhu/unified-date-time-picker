package com.example.datetimedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.bobgenix.datetimedialog.AndroidUtilities
import com.bobgenix.datetimedialog.BottomSheet
import com.bobgenix.datetimedialog.DialogHelper
import com.example.datetimedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
                null
            )
            Log.d("aaaa", "after clicked.....")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("aaaa", "back pressed...")
    }
}