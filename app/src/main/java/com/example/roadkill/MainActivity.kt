package com.example.roadkill

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.roadkill.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onStart() {
        super.onStart()
        Log.d("실행", "yes")
        RequestPermissionsUtil(this).requestLocation() // 위치 권한 요청
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermission()

        binding.clNearmissReport.setOnClickListener{
            MyApplication.prefs.setString("selection", "nearmiss")
            val intent = Intent(applicationContext, NearmissTimeActivity::class.java)
            startActivity(intent)
        }

        binding.clSmallAnimal.setOnClickListener{
            MyApplication.prefs.setString("selection", "small")
            val intent = Intent(applicationContext, TimeActivity::class.java)
            startActivity(intent)
        }

        binding.clBigAnimal.setOnClickListener{
            MyApplication.prefs.setString("selection", "big")
            val intent = Intent(applicationContext, TimeActivity::class.java)
            startActivity(intent)
        }

        // initNavigationBar() //네이게이션 바의 각 메뉴 탭을 눌렀을 때 화면이 전환되도록 하는 함수.
    }

    // 안드로이드 API 30 버전부터는 backgroundPermission 을 직접 설정해야함
    private fun backgroundPermission(){
        Log.d("debug", "다이얼로그")
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            ), 2)
    }

    private fun requestPermission(){
        // 이미 권한이 있으면 그냥 리턴
        if(hasLocationPermissions()){
            return
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1)
                //permissionDialog(this)
            }
            // API 23 미만 버전에서는 ACCESS_BACKGROUND_LOCATION X
            else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1)
            }
        }
    }

    private fun hasLocationPermissions(): Boolean {
        val permission1 = Manifest.permission.ACCESS_FINE_LOCATION
        val permission2 = Manifest.permission.ACCESS_COARSE_LOCATION

        return (ActivityCompat.checkSelfPermission(this, permission1) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, permission2) == PackageManager.PERMISSION_GRANTED)
    }

    // 백그라운드 권한 요청
    private fun permissionDialog(context : Context){
        var builder = AlertDialog.Builder(context)
        builder.setTitle("백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.")

        var listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE ->
                    backgroundPermission()
            }
        }
        builder.setPositiveButton("네", listener)
        builder.setNegativeButton("아니오", null)

        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        return when (item.itemId) {
            R.id.menu_item1 -> {
                MyApplication.prefs.setString("id", "no id")
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_item2 -> {
                val intent = Intent(applicationContext, UserMyReportActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
