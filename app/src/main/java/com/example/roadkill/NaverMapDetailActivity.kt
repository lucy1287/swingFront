package com.example.roadkill

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.roadkill.databinding.ActivityNaverMapDetailBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.math.pow
import kotlin.math.roundToInt

class NaverMapDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LOCATION_PERMISSION_REQUEST_CODE = 5000
    private var nowLatitude: Double = 0.0
    private var nowLongitude: Double = 0.0
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var binding: ActivityNaverMapDetailBinding
    private lateinit var naverMap: NaverMap
    private val marker = Marker()
    private lateinit var locationSource: FusedLocationSource

    // onCreate에서 권한을 확인하며 위치 권한이 없을 경우 사용자에게 권한을 요청한다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaverMapDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initMapView()
        }

        binding.ivMapSearch.setOnClickListener {
            var mapSearchInput = binding.etMapSearch.text.toString()
            // var mapSearchInput="서울특별시 동작구"
            search(mapSearchInput)
            binding.etMapSearch.clearFocus()
        }

        binding.tvBtn2.setOnClickListener{
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
    }

    private fun initMapView() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment_detail) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment_detail, it).commit()
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
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow  //위치 추적하면서 카메라도 따라 이동

        naverMap.setOnMapLongClickListener { point, coord ->  // 지도 롱 클릭 시 마커 생성
            Toast.makeText(
                this, "${coord.latitude.roundTo(6)}, ${coord.longitude.roundTo(6)}",
                Toast.LENGTH_SHORT
            ).show()
            marker.position = LatLng(coord.latitude, coord.longitude)
            marker.map = naverMap
            MyApplication.prefs.setString("lat", coord.latitude.roundTo(6).toString())
            MyApplication.prefs.setString("lng",coord.longitude.roundTo(6).toString())
            Log.d("위도", MyApplication.prefs.getString("lat", ""))

            binding.tvBtn2.setBackgroundResource(R.drawable.green_round_background)
            binding.tvBtn2.isEnabled = true
        }
    }

    fun search(mapSearchInput: String) = runBlocking {
        val addr: String = URLEncoder.encode(mapSearchInput, "UTF-8")

        // 카카오 REST API 키
        val apiKey = "baafde3b7e2b0ca2301e515aac5d1492"

        // 검색 API 호출
        val searchUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?query=$addr"
        val response = withContext(Dispatchers.IO) { sendGetRequest(searchUrl, apiKey) }

        // 검색 결과 파싱
        val places = mutableListOf<Pair<String, String>>() // 장소명과 좌표를 저장할 리스트
        val jsonObject = JSONObject(response)
        val documents = jsonObject.getJSONArray("documents")
        for (i in 0 until documents.length()) {
            val document = documents.getJSONObject(i)
            val placeName = document.getString("place_name")
            val x = document.getString("x")
            val y = document.getString("y")
            places.add(Pair(placeName, "$x,$y"))
        }

        // 첫 번째 검색 결과로 지도 이동
        if (places.isNotEmpty()) {
            val firstPlace = places[0]
            val mapUrl = "https://map.kakao.com/link/map/${firstPlace.second}"
            println("장소명: ${firstPlace.first}")
            println("좌표: ${firstPlace.second}")
            println("지도 링크: $mapUrl")

            val coordinates = firstPlace.second.split(",") // 콤마로 분리하여 리스트로 저장
            val longitude = coordinates[0].toDouble().roundTo(6)// 경도
            val latitude = coordinates[1].toDouble().roundTo(6) // 위도
            Log.d("latitude", "latitude" + latitude.toString())
            Log.d("longitude", "longitude" + longitude.toString())

            val latLng = LatLng(latitude, longitude) // 위도와 경도를 지정하세요
            val cameraPosition = CameraPosition(latLng, 16.0) // 중심점과 줌 레벨을 설정합니다
            naverMap.cameraPosition = cameraPosition
        } else {
            Toast.makeText(applicationContext, "검색결과가 없습니다. 다시 검색해 주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendGetRequest(urlString: String, apiKey: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Authorization", "KakaoAK $apiKey")

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuffer()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            return response.toString()
        } else {
            throw Exception("HTTP GET request failed with response code: $responseCode")
        }
    }

    fun Double.roundTo(decimals: Int): Double {
        val factor = 10.0.pow(decimals.toDouble())
        return (this * factor).roundToInt() / factor
    }

//    @SuppressLint("MissingPermission")
//    private fun getLocation() {
//        val fusedLocationProviderClient =
//            LocationServices.getFusedLocationProviderClient(this)
//
//        fusedLocationProviderClient.lastLocation
//            .addOnSuccessListener { success: Location? ->
//                success?.let { location ->
//                    nowLatitude = location.latitude.roundTo(6)
//                    nowLongitude = location.longitude.roundTo(6)
//                    Log.d("현재 위경도1","${nowLatitude}, ${nowLongitude}")
//
//                    val latLng = LatLng(nowLatitude, nowLongitude) // 위도와 경도를 지정하세요
//                    val cameraPosition = CameraPosition(latLng, 16.0) // 중심점과 줌 레벨을 설정합니다
//                    naverMap.cameraPosition = cameraPosition
//
//                    Log.d("현재 위경도2","${nowLatitude}, ${nowLongitude}")
//                }
//            }
//            .addOnFailureListener { fail ->
//                Log.d("getLocation",fail.localizedMessage)
//            }
//  }

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