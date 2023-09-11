package com.example.roadkill

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.roadkill.databinding.ActivityNaverMapBinding
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource


// OnMapReadyCallback을 상속 받는다.
class NaverMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityNaverMapBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val LOCATION_PERMISSION_REQUEST_CODE = 5000
    private val marker = Marker()

    private val PERMISSIONS = arrayOf(
        ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
    )

    // onCreate에서 권한을 확인하며 위치 권한이 없을 경우 사용자에게 권한을 요청한다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaverMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initMapView()
        }

        binding.tvBtn1.setOnClickListener{
            binding.root.removeAllViews()
            if(MyApplication.prefs.getString("selection", "") == "big"){
                val intent = Intent(applicationContext, InjuredActivity::class.java)
                startActivity(intent)
            }
            else if(MyApplication.prefs.getString("selection", "") == "small"){
                val intent = Intent(applicationContext, AnimalStatusActivity::class.java)
                startActivity(intent)
            }
            else if(MyApplication.prefs.getString("selection", "") == "nearmiss"){
                val intent = Intent(applicationContext, NearmissAnimalSizeActivity::class.java)
                startActivity(intent)
            }
        }

        binding.tvBtn2.setOnClickListener{
            binding.root.removeAllViews()
            val intent = Intent(applicationContext, NaverMapDetailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initMapView() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }

        // fragment의 getMapAsync() 메서드로 OnMapReadyCallback 콜백을 등록하면 비동기로 NaverMap 객체를 얻을 수 있다.
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    // hasPermission()에서는 위치 권한이 있을 경우 true를, 없을 경우 false를 반환한다.
    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        // 현재 위치
        naverMap.locationSource = locationSource
        // 현재 위치 버튼 기능
        naverMap.uiSettings.isLocationButtonEnabled = true
        // 위치를 추적하면서 카메라도 따라 움직인다.
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }
}