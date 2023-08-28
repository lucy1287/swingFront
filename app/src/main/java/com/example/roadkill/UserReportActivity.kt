package com.example.roadkill

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ContentValues
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roadkill.databinding.ActivityUserReportBinding
import com.example.roadkill.databinding.ActivityUserReportDetailBinding
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat

class UserReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserReportBinding
    var reportImageList: ArrayList<Uri?> = ArrayList<Uri?>()
    lateinit var reportImageRVAdapter: ReportImageRVAdapter

    override fun onStart() {
        super.onStart()
        RequestPermissionsUtil(this).requestLocation() // 위치 권한 요청
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserReportBinding.inflate(layoutInflater)
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

        //사진
            //갤러리에서 이미지 가져오기
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 1)


        //요청하기 버튼
        binding.tvBtnOk.setOnClickListener{
                Toast.makeText(applicationContext, "요청이 접수되었습니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
        }

        reportImageRVAdapter = ReportImageRVAdapter(reportImageList, this)
        Log.d("reportImageList", reportImageList.toString())
        reportImageRVAdapter.setOnItemClickListener(object : ReportImageRVAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int) {
//                val intent = Intent(applicationContext, ImageShowActivity::class.java)
//                intent.putExtra("uri", reportImageList[pos].uri.toString())
//                Log.d("uri:", reportImageList[pos].uri.toString())
//                startActivity(intent)
            }
        })

        val reportImageLinearLayoutManager = LinearLayoutManager(this)
        reportImageLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvAccidentImage.layoutManager = reportImageLinearLayoutManager
        binding.rvAccidentImage.adapter = reportImageRVAdapter
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

    private fun getTime(textView: TextView){
        val currentTime : Long = System.currentTimeMillis() // ms로 반환
        val dataFormat5 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        textView.text = dataFormat5.format(currentTime)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1 -> {
                if (resultCode == RESULT_OK) {
                    if(data == null){   // 어떤 이미지도 선택하지 않은 경우
                        Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(data.clipData == null){     //이미지 1개 선택
                            Log.e("single choice: ", data.data.toString());
                            var imageUri : Uri? = data.data
                            reportImageList.add(imageUri);

                            reportImageRVAdapter = ReportImageRVAdapter(reportImageList, this);
                            binding.rvAccidentImage.adapter = reportImageRVAdapter
                            val reportImageLinearLayoutManager = LinearLayoutManager(this)
                            reportImageLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                            binding.rvAccidentImage.layoutManager = reportImageLinearLayoutManager
                        }
                        else{      // 이미지를 여러장 선택한 경우
                            var clipData : ClipData = data.clipData!!;

                            if(clipData.itemCount > 10){
                                Toast.makeText(this, "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                            }
                            else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                                Log.e(ContentValues.TAG, "multiple choice");

                                for (i in 0 until clipData.itemCount) {
                                    val imageUri: Uri? = clipData.getItemAt(i).uri
                                    try {
                                        reportImageList.add(imageUri)
                                    } catch (e: Exception) {
                                        Log.e(ContentValues.TAG, "File select error", e)
                                    }
                                }
                                Log.d("이미지 리스트", reportImageList.toString())
                                reportImageRVAdapter = ReportImageRVAdapter(reportImageList, this);
                                binding.rvAccidentImage.adapter = reportImageRVAdapter
                                val reportImageLinearLayoutManager = LinearLayoutManager(this)
                                reportImageLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                                binding.rvAccidentImage.layoutManager = reportImageLinearLayoutManager
                            }
                        }
                    }
                }
            }
        }
    }
}
