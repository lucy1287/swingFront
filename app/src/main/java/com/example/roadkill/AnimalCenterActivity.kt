package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityAnimalCenterBinding

class AnimalCenterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAnimalCenterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimalCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}