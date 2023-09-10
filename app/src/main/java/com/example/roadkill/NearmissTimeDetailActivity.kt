package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityNearmissTimeBinding
import com.example.roadkill.databinding.ActivityNearmissTimeDetailBinding

class NearmissTimeDetailActivity:AppCompatActivity() {

    private lateinit var binding: ActivityNearmissTimeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearmissTimeDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, NaverMapActivity::class.java)
            startActivity(intent)
        }
    }
}