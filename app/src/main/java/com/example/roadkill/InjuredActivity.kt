package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityInjuredBinding

class InjuredActivity: AppCompatActivity() {
    private lateinit var binding: ActivityInjuredBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInjuredBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBtn1.setOnClickListener{
            val intent = Intent(applicationContext, Call911Activity::class.java)
            startActivity(intent)
        }

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, AnimalStatusActivity::class.java)
            startActivity(intent)
        }
    }
}