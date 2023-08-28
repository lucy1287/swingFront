package com.example.roadkill

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityMapDetailBinding
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.math.pow
import kotlin.math.roundToInt

class MapDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapDetailBinding
    private lateinit var mapView: MapView
    private var nowLatitude: Double = 0.0
    private var nowLongitude: Double = 0.0
    private var markerTag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.mapViewDetail
        mapView.setDaumMapApiKey("baafde3b7e2b0ca2301e515aac5d1492")

        // getLocation()

//        val mapPoint = MapPoint.mapPointWithGeoCoord(nowLatitude, nowLongitude)
//        mapView.setMapCenterPoint(mapPoint, true)
//        mapView.setZoomLevel(3, true) // 초기 줌 레벨 설정
//        mapView.invalidate()

        binding.ivMapSearch.setOnClickListener {
            var mapSearchInput = binding.etMapSearch.text.toString()
            // var mapSearchInput="서울특별시 동작구"
            main(mapSearchInput)
            binding.etMapSearch.clearFocus()
        }

        binding.tvBtn2.setOnClickListener{
            val intent = Intent(applicationContext, CameraActivity::class.java)
            startActivity(intent)
        }
    }

    fun main(mapSearchInput: String) = runBlocking {
        val keyword = "카카오판교오피스" // 검색할 키워드
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

            //  val mapView = findViewById<MapView>(R.id.map_view)
            val mapPoint = MapPoint.mapPointWithGeoCoord(latitude, longitude)
            mapView.setMapCenterPoint(mapPoint, true)
            mapView.setZoomLevel(3, true) // 초기 줌 레벨 설정
            mapView.invalidate()
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
}