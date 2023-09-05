package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityCameraBinding
import com.example.roadkill.databinding.ActivityNearmissAnimalSizeBinding

class NearmissAnimalSizeActivity: AppCompatActivity() {

    private lateinit var binding: ActivityNearmissAnimalSizeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearmissAnimalSizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBtn1.setOnClickListener{
            val intent = Intent(applicationContext, UserNearmissReportActivity::class.java)
            startActivity(intent)
        }

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, UserNearmissReportActivity::class.java)
            startActivity(intent)
        }
    }
}