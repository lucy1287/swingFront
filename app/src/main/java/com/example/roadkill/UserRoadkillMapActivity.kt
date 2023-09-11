package com.example.roadkill

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.roadkill.api.ManagerReportResponse
import com.example.roadkill.api.ReportService
import com.example.roadkill.databinding.ActivityUserRoadkillMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserRoadkillMapActivity: AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityUserRoadkillMapBinding
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val LOCATION_PERMISSION_REQUEST_CODE = 5000
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserRoadkillMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initMapView()
        }


  //      val result = runBlocking {
  //          getReportLatLngFun()
  //      }
  //      Log.d("리포트리스트1", result.toString())
//        // CoroutineScope를 생성합니다.
//        val scope = CoroutineScope(Dispatchers.Default)
//
//        // 비동기 작업을 시작합니다.
//        val job = scope.async {
//            // 여기에 비동기 작업을 수행하는 코드를 작성합니다.
//            getReportLatLngFun()
//        }
//
//        // 비동기 작업이 완료될 때까지 대기합니다.
//        runBlocking {
//            val result = job.await()
//
//            // 이후에 실행할 코드를 여기에 작성합니다.
//            Log.d("리포트리스트2", result.toString())
//
//            for (latLng in result) {
//                val marker = Marker()
//                marker.position = latLng
//                marker.map = naverMap
//            }
//        }
//
//        // CoroutineScope를 닫습니다.
//        scope.cancel()

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

        getReportLatLngFun(naverMap)
    }


    private fun getReportLatLngFun(naverMap: NaverMap){
        ReportService.retrofitGetManagerReport().enqueue(object:
            Callback<List<ManagerReportResponse>> {
            override fun onResponse(
                call: Call<List<ManagerReportResponse>>,
                response: Response<List<ManagerReportResponse>>
            ) {
                if (response.isSuccessful) {
                    val managerReports = response.body()
                    Log.d("매니저리포츠", managerReports.toString())
                    if (managerReports != null) {
                        // JSON 배열을 반복하여 각각의 MyReportResponse 객체를 처리할 수 있습니다.
                        for (report in managerReports) {
                            Log.d("리포트", report.toString())
                            val lat = report.lat
                            val lng = report.lng
                            val marker = Marker()
                            marker.position = LatLng(lat, lng)
                            marker.map = naverMap
                        }
                    } else {
                        try {
                            val body = response.errorBody()!!.string()
                            Log.e(ContentValues.TAG, "body : $body")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<ManagerReportResponse>>, t: Throwable) {
                Log.e("TAG", "실패원인: {$t}")
            }
        })
    }
}