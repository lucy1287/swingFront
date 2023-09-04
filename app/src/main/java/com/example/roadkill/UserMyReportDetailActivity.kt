package com.example.roadkill

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.roadkill.api.MyReportResponse
import com.example.roadkill.api.ReportService
import com.example.roadkill.databinding.ActivityUserMyReportDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserMyReportDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityUserMyReportDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMyReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val _id = intent.getStringExtra("_id")
        if (_id != null) {
            postMyReportDetailFun(_id)
        }
    }

    private fun postMyReportDetailFun(_id: String) {

        ReportService.retrofitGetReportDetail()
            .enqueue(object : Callback<MyReportResponse> { // 응답 타입을 String으로 지정
                override fun onResponse(
                    call: Call<MyReportResponse>,
                    response: Response<MyReportResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        println("MyReportDetail 불러오기 성공: $responseData")
                        if (responseData != null) {
                            binding.tvAccidentDateInfo.text = responseData.accidentTime
                            binding.tvAccidentLocationInfo.text = "(" + responseData.lat + " , " + responseData.lng + ")"

                            //이미지 표시
                            val imgPart = ApiClient.BASE_URL + responseData.img.replace("\\", "/")
                            Glide.with(this@UserMyReportDetailActivity) // YourActivity를 현재 활성화된 액티비티로 변경해야 합니다.
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