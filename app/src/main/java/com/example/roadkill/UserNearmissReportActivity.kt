package com.example.roadkill

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.api.MyReportResponse
import com.example.roadkill.api.NearmissRequest
import com.example.roadkill.api.NearmissService
import com.example.roadkill.databinding.ActivityUserNearmissReportBinding
import com.example.roadkill.databinding.ActivityUserReportBinding
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class UserNearmissReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserNearmissReportBinding
    private var lat: Double = 0.0
    private var lng: Double = 0.0


    override fun onStart() {
        super.onStart()
        RequestPermissionsUtil(this).requestLocation() // 위치 권한 요청
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserNearmissReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        RequestPermissionsUtil(this).requestLocation()

        //위경도 좌표
        val tvAccidentLocationInfo: TextView = findViewById(R.id.tv_accident_location_info)
        getLocation(tvAccidentLocationInfo)
        val ivAccidentLocationUpdate: ImageView = findViewById(R.id.iv_accident_location_update)
        ivAccidentLocationUpdate.setOnClickListener{
            getLocation(tvAccidentLocationInfo)
        }

        val tvAccidentDateInfo: TextView = findViewById(R.id.tv_accident_date_info)
        getTime(tvAccidentDateInfo)
        val ivAccidentDateUpdate: ImageView = findViewById(R.id.iv_accident_date_update)
        ivAccidentDateUpdate.setOnClickListener{
            getTime(tvAccidentDateInfo)
        }


        //요청하기 버튼
        binding.tvBtnOk.setOnClickListener{
            postReportFun()
            Toast.makeText(applicationContext, "요청이 접수되었습니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLocation(textView: TextView) {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    textView.text =
                        "${location.latitude}, ${location.longitude}"
                    lat = location.latitude
                    lng = location.longitude
                }
            }
            .addOnFailureListener { fail ->
                textView.text = fail.localizedMessage
            }
    }

    private fun getTime(textView: TextView){
        val currentTime : Long = System.currentTimeMillis() // ms로 반환
        val dataFormat5 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        textView.text = dataFormat5.format(currentTime)
    }


    private fun postReportFun() {
        var size: Int = 0
        val lat = lat
        val lng = lng
        var nearmissJson = NearmissRequest(size, lat, lng)
        Log.d("dsds", nearmissJson.toString())

        NearmissService.retrofitPostNearmissReport(nearmissJson, MyApplication.prefs.getString("id", "no id"))
            .enqueue(object : Callback<String> { // 응답 타입을 String으로 지정
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        println("Nearmiss 신고 성공: $responseData")
                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("Nearmiss 신고 실패: $errorBody")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("TAG", "실패원인: $t")
                }
            })
    }


}
