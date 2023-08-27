package com.example.roadkill

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.roadkill.databinding.ActivityManagerHistoryBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ManagerHistoryActivity: AppCompatActivity() {
    private lateinit var binding: ActivityManagerHistoryBinding
    var accidentHistoryList: ArrayList<History> = ArrayList<History>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val statusVPAdapter = StatusVPAdapter(
            supportFragmentManager,
            lifecycle
        )
        val viewPager2 = findViewById<ViewPager2>(R.id.vp_status)
        viewPager2.adapter = statusVPAdapter

        val tabLayout = findViewById<TabLayout>(R.id.tl_status)
        TabLayoutMediator(
            tabLayout, viewPager2
        ) { tab, position ->
            if (position == 0) tab.text = "진행 중" else if (position == 1) tab.text = "처리 완료"
        }.attach()
    }
}