package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityTimeBinding

class TimeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvBtn1.setOnClickListener{
            val intent = Intent(applicationContext, MapActivity::class.java)
            startActivity(intent)
        }

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, TimeDetailActivity::class.java)
            startActivity(intent)
        }
    }
}