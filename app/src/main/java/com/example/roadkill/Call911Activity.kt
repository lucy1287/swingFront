package com.example.roadkill

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityCall911Binding
import com.example.roadkill.databinding.ActivityCameraBinding

class Call911Activity: AppCompatActivity() {
    private lateinit var binding: ActivityCall911Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCall911Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivCall911.setOnClickListener{
            var intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:119")
            if(intent.resolveActivity(packageManager) != null){
                startActivity(intent)
            }
        }

        binding.tvBtn2.setOnClickListener{
            MyApplication.prefs.setString("selection", "")
            val intent = Intent(applicationContext, MainActivity::class.java)
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