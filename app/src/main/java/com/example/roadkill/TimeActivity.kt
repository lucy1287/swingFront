package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityTimeBinding
import java.text.SimpleDateFormat

class TimeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvBtn1.setOnClickListener{
            MyApplication.prefs.setString("time", getTime())
            val intent = Intent(applicationContext, NaverMapActivity::class.java)
            startActivity(intent)
        }

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, TimeDetailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getTime(): String{
        val currentTime : Long = System.currentTimeMillis() // ms로 반환
        val dataFormat5 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        return dataFormat5.toString()
    }
}