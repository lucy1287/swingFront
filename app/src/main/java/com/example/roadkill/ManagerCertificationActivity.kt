package com.example.roadkill

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityManagerCertificationBinding

class ManagerCertificationActivity: AppCompatActivity() {
    private lateinit var binding: ActivityManagerCertificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerCertificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}