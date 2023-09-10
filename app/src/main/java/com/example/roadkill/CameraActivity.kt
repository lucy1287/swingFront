package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityCameraBinding

class CameraActivity:AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBtn1.setOnClickListener{
            val intent = Intent(applicationContext, UserReportActivity::class.java)
            intent.putExtra("imageMethod", "camera")
            startActivity(intent)
        }

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, UserReportActivity::class.java)
            intent.putExtra("imageMethod", "gallery")
            startActivity(intent)
        }
    }
}