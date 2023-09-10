package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityTimeDetailBinding

class TimeDetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTimeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, NaverMapActivity::class.java)
            startActivity(intent)
        }
    }
}