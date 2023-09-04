package com.example.roadkill

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.roadkill.api.MyReportResponse
import com.example.roadkill.api.RegisterService
import com.example.roadkill.api.ReportService
import com.example.roadkill.databinding.ActivityUserMyReportBinding
import com.example.roadkill.databinding.ActivityUserNearmissReportBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class UserMyReportActivity: AppCompatActivity() {

    private lateinit var binding: ActivityUserMyReportBinding
    var accidentHistoryList: ArrayList<History> = ArrayList<History>()
    private lateinit var userMyReportRVAdapter: UserMyReportRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMyReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userMyReportRVAdapter = UserMyReportRVAdapter(accidentHistoryList)
        userMyReportRVAdapter.setOnItemClickListener(object : UserMyReportRVAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int) {
                val _id = accidentHistoryList[pos]._id
                val intent = Intent(applicationContext, UserMyReportDetailActivity::class.java)
                intent.putExtra("_id", _id)
                startActivity(intent)
            }
        })

        val userMyReportLinearLayoutManager = LinearLayoutManager(this)
        userMyReportLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvMyReport.layoutManager = userMyReportLinearLayoutManager
        binding.rvMyReport.adapter = userMyReportRVAdapter

        getMyReportFun()
    }

    private fun getMyReportFun(){
        //var email=binding.etSignupEmailleft.text.toString()+binding.etSignupEmailright.text.toString()
        ReportService.retrofitGetMyReport().enqueue(object: Callback<List<MyReportResponse>> {
            override fun onResponse(
                call:  Call<List<MyReportResponse>>,
                response: Response<List<MyReportResponse>>
            ) {
                if (response.isSuccessful) {
                    val myReports = response.body()
                    if (myReports != null) {
                        // JSON 배열을 반복하여 각각의 MyReportResponse 객체를 처리할 수 있습니다.
                        for (report in myReports) {
                            val imgPart = ApiClient.BASE_URL + report.img.replace("\\", "/")
                            //Glide.with(this@UserMyReportActivity) // YourActivity를 현재 활성화된 액티비티로 변경해야 합니다.
                            //    .load(imgPart) // MultipartBody.Part에서 이미지를 로드합니다.
                            //    .diskCacheStrategy(DiskCacheStrategy.NONE) // 캐시 사용하지 않음 (선택 사항)
                             //   .skipMemoryCache(true) // 메모리 캐시 사용하지 않음 (선택 사항)
                            //    .into(binding.imageView) // 이미지를 표시할 ImageView

                            val _id = report._id
                            val lat = report.lat
                            val lng = report.lng
                            val species = report.species
                            val cause = report.cause
                            val otherInfo = report.otherInfo
                            val status = report.status
                            val accidentTime = report.accidentTime
                            // 여기에서 데이터를 사용하세요.
                            accidentHistoryList.add(History(_id, accidentTime, species))
                            Log.d("accidentHistoryList", accidentHistoryList.toString())
                            userMyReportRVAdapter.notifyDataSetChanged();
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
            override fun onFailure(call: Call<List<MyReportResponse>>, t: Throwable) {
                Log.e("TAG", "실패원인: {$t}")
            }
        })
    }

}