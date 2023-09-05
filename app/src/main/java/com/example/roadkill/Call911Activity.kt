package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityCall911Binding
import com.example.roadkill.databinding.ActivityCameraBinding

class Call911Activity: AppCompatActivity() {
    private lateinit var binding: ActivityCall911Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCall911Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBtn1.setOnClickListener{
            MyApplication.prefs.setString("selection", "")
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }
}