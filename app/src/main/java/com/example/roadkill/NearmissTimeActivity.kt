package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityNearmissTimeBinding
import com.example.roadkill.databinding.ActivityTimeBinding
import java.text.SimpleDateFormat

class NearmissTimeActivity:AppCompatActivity() {
    private lateinit var binding: ActivityNearmissTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearmissTimeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvBtn1.setOnClickListener{
            MyApplication.prefs.setString("dateInfo", getTime())
            val intent = Intent(applicationContext, NaverMapActivity::class.java)
            startActivity(intent)
        }

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, NearmissTimeDetailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getTime(): String{
        val currentTime : Long = System.currentTimeMillis() // ms로 반환
        val dataFormat5 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        return dataFormat5.format(currentTime)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        return when (item.itemId) {
            R.id.item1 -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.item2 -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}