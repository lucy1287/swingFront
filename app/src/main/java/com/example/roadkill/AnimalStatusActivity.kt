package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityAnimalStatusBinding
import com.example.roadkill.databinding.ActivityCameraBinding

class AnimalStatusActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAnimalStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimalStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBtn1.setOnClickListener{
            val intent = Intent(applicationContext, AnimalCenterActivity::class.java)
            startActivity(intent)
        }

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, CameraActivity::class.java)
            startActivity(intent)
        }

        binding.tvBtn3.setOnClickListener{
            val intent = Intent(applicationContext, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}