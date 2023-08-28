package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityCameraBinding
import com.example.roadkill.databinding.ActivitySignUpBinding

class CameraActivity:AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, UserReportActivity::class.java)
            startActivity(intent)
        }
    }
}