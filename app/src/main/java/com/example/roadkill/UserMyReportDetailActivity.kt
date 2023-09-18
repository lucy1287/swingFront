package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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

        val rid = intent.getStringExtra("rid")
        if (rid != null) {
            getMyReportDetailFun(rid)
        }

        binding.tvBtnOk.setOnClickListener {
            val intent = Intent(applicationContext, UserMyReportActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getMyReportDetailFun(rid: String) {

        ReportService.retrofitGetReportDetail(rid)
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
                            binding.tvAccidentCauseInfo.text = responseData.cause
                            binding.tvAccidentOtherInfo.text= responseData.otherInfoByUser
                            if(responseData.species == "너구리" || responseData.species == "노루" ||
                                responseData.species == "고라니" || responseData.species == "멧돼지")
                                binding.tvSpeciesResultInfo.text = responseData.species
                            else if(responseData.species == "raccoon")
                                 binding.tvSpeciesResultInfo.text = "너구리"
                            else if(responseData.species == "roe deer")
                                 binding.tvSpeciesResultInfo.text = "노루"
                            else if(responseData.species == "water deer")
                                binding.tvSpeciesResultInfo.text = "고라니"
                            else if(responseData.species == "wild boar")
                                binding.tvSpeciesResultInfo.text = "멧돼지"

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