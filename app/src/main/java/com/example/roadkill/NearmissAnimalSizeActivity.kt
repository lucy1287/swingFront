package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityCameraBinding
import com.example.roadkill.databinding.ActivityNearmissAnimalSizeBinding

class NearmissAnimalSizeActivity: AppCompatActivity() {

    private lateinit var binding: ActivityNearmissAnimalSizeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearmissAnimalSizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSmall.setOnClickListener{
            val intent = Intent(applicationContext, UserNearmissReportActivity::class.java)
            startActivity(intent)
        }

        binding.ivBig.setOnClickListener{
            val intent = Intent(applicationContext, UserNearmissReportActivity::class.java)
            startActivity(intent)
        }
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