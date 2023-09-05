package com.example.roadkill

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityMapBinding
import com.google.android.gms.location.LocationServices
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import kotlin.math.pow
import kotlin.math.roundToInt

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var mapView: MapView
    private var nowLatitude: Double = 0.0
    private var nowLongitude: Double = 0.0
    private var markerTag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.mapView
        mapView.setDaumMapApiKey("baafde3b7e2b0ca2301e515aac5d1492")

         getLocation()

         val mapPoint = MapPoint.mapPointWithGeoCoord(nowLatitude, nowLongitude)
         mapView.setMapCenterPoint(mapPoint, true)
         mapView.setZoomLevel(3, true) // 초기 줌 레벨 설정
         mapView.invalidate()
         setMarker(mapPoint)


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
            val intent = Intent(applicationContext, MapDetailActivity::class.java)
            startActivity(intent)
        }
    }


    fun Double.roundTo(decimals: Int): Double {
        val factor = 10.0.pow(decimals.toDouble())
        return (this * factor).roundToInt() / factor
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    nowLatitude = location.latitude.roundTo(6)
                    nowLongitude = location.longitude.roundTo(6)
                    Log.d("현재 위경도1","${nowLatitude}, ${nowLongitude}")

                    val mapPoint = MapPoint.mapPointWithGeoCoord(nowLatitude, nowLongitude)
                    mapView.setMapCenterPoint(mapPoint, true)
                    mapView.setZoomLevel(3, true) // 초기 줌 레벨 설정
                    //  mapView.invalidate()

                    Log.d("현재 위경도2","${nowLatitude}, ${nowLongitude}")
                }
            }
            .addOnFailureListener { fail ->
                Log.d("getLocation",fail.localizedMessage)
            }
    }

    private fun setMarker(mapPoint: MapPoint){
        val marker = MapPOIItem()
        marker.itemName = "마커" // 마커 이름
        marker.tag = markerTag// 마커 고유 태그 (원하는 값으로 설정)
        marker.mapPoint = mapPoint // 중심 좌표에 마커 추가
        marker.markerType = MapPOIItem.MarkerType.RedPin // 마커 아이콘 타입
        marker.selectedMarkerType = MapPOIItem.MarkerType.BluePin // 마커 선택 시 아이콘 타입
        mapView.addPOIItem(marker)
        markerTag++
    }

    override fun onResume() {
        super.onResume()
        if (mapView != null) {
            mapView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mapView != null) {
            mapView.onPause()
        }
    }

}





