package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityNearmissTimeBinding
import com.example.roadkill.databinding.ActivityTimeBinding

class NearmissTimeActivity:AppCompatActivity() {
    private lateinit var binding: ActivityNearmissTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearmissTimeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvBtn1.setOnClickListener{
            val intent = Intent(applicationContext, NaverMapActivity::class.java)
            startActivity(intent)
        }

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, NearmissTimeDetailActivity::class.java)
            startActivity(intent)
        }
    }
}