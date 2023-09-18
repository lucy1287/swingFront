package com.example.roadkill

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.roadkill.api.ManagerUpdateRequest
import com.example.roadkill.api.MyReportResponse
import com.example.roadkill.api.ReportService
import com.example.roadkill.databinding.ActivityManagerReceiptDetailTrueBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManagerReceiptDetailTrueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManagerReceiptDetailTrueBinding
    private var receiptImageList: ArrayList<Uri?> = ArrayList<Uri?>()
    private lateinit var receiptImageRVAdapter: ReceiptImageRVAdapter
    private lateinit var rid: String

    override fun onStart() {
        super.onStart()
        RequestPermissionsUtil(this).requestLocation() // 위치 권한 요청
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerReceiptDetailTrueBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //RequestPermissionsUtil(this).requestLocation()

        rid = intent.getStringExtra("rid").toString()
        if (rid != null) {
            getManagerDetailFun(rid)
        }

        //처리하기 버튼
        binding.tvBtnOk.setOnClickListener{
            val intent = Intent(applicationContext, ManagerHistoryActivity::class.java)
            startActivity(intent)
        }

//        receiptImageRVAdapter = ReceiptImageRVAdapter(receiptImageList, this)
        //       Log.d("receiptImageList", receiptImageList.toString())
        //       receiptImageRVAdapter.setOnItemClickListener(object : ReceiptImageRVAdapter.OnItemClickListener {
        //           override fun onItemClick(pos: Int) {
//                val intent = Intent(applicationContext, ImageShowActivity::class.java)
//                intent.putExtra("uri", reportImageList[pos].uri.toString())
//                Log.d("uri:", reportImageList[pos].uri.toString())
//                startActivity(intent)
        //           }
        //       })

        //       val receiptImageLinearLayoutManager = LinearLayoutManager(this)
        //       receiptImageLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        //       binding.rvAccidentImage.layoutManager = receiptImageLinearLayoutManager
        //       binding.rvAccidentImage.adapter = receiptImageRVAdapter
    }


    private fun getManagerDetailFun(rid: String) {

        ReportService.retrofitGetReportDetail(rid)
            .enqueue(object : Callback<MyReportResponse> { // 응답 타입을 String으로 지정
                override fun onResponse(
                    call: Call<MyReportResponse>,
                    response: Response<MyReportResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        println("ReportDetail 불러오기 성공: $responseData")
                        if (responseData != null) {
                            binding.tvAccidentDateInfo.text = responseData.accidentTime
                            binding.tvAccidentLocationInfo.text = "(" + responseData.lat + " , " + responseData.lng + ")"
                            binding.tvAccidentCauseInfo.text = responseData.cause
                            binding.tvAccidentOtherInfo.text= responseData.otherInfoByUser
                            binding.tvSpeciesResultInfo.text = responseData.species

                            //이미지 표시
                            val imgPart = ApiClient.BASE_URL + responseData.img.replace("\\", "/")
                            Glide.with(this@ManagerReceiptDetailTrueActivity) // YourActivity를 현재 활성화된 액티비티로 변경해야 합니다.
                                .load(imgPart) // MultipartBody.Part에서 이미지를 로드합니다.
                                .diskCacheStrategy(DiskCacheStrategy.NONE) // 캐시 사용하지 않음 (선택 사항)
                                .skipMemoryCache(true) // 메모리 캐시 사용하지 않음 (선택 사항)
                                .into(binding.ivAccidentImage) // 이미지를 표시할 ImageView
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("MyReportDetail 불러오기 실패: $errorBody")
                    }
                }

                override fun onFailure(call: Call<MyReportResponse>, t: Throwable) {
                    Log.e("TAG", "실패원인: $t")
                }
            })
    }

}




