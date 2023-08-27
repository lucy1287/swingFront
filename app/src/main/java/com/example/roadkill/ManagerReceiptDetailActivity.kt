package com.example.roadkill

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roadkill.databinding.ActivityManagerReceiptDetailBinding
import com.google.android.gms.location.LocationServices

class ManagerReceiptDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManagerReceiptDetailBinding
    var receiptImageList: ArrayList<Uri?> = ArrayList<Uri?>()
    lateinit var receiptImageRVAdapter: ReceiptImageRVAdapter

    override fun onStart() {
        super.onStart()
        RequestPermissionsUtil(this).requestLocation() // 위치 권한 요청
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerReceiptDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        RequestPermissionsUtil(this).requestLocation()

        //처리하기 버튼
        binding.tvBtnOk.setOnClickListener{
                Toast.makeText(applicationContext, "처리가 완료되었습니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, UserMainActivity::class.java)
                startActivity(intent)
        }

        receiptImageRVAdapter = ReceiptImageRVAdapter(receiptImageList, this)
        Log.d("receiptImageList", receiptImageList.toString())
        receiptImageRVAdapter.setOnItemClickListener(object : ReceiptImageRVAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int) {
//                val intent = Intent(applicationContext, ImageShowActivity::class.java)
//                intent.putExtra("uri", reportImageList[pos].uri.toString())
//                Log.d("uri:", reportImageList[pos].uri.toString())
//                startActivity(intent)
            }
        })

        val receiptImageLinearLayoutManager = LinearLayoutManager(this)
        receiptImageLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvAccidentImage.layoutManager = receiptImageLinearLayoutManager
        binding.rvAccidentImage.adapter = receiptImageRVAdapter
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
                }
            }
            .addOnFailureListener { fail ->
                textView.text = fail.localizedMessage
            }
    }
}




