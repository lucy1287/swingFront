package com.example.roadkill

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roadkill.api.ManagerReportResponse
import com.example.roadkill.api.MyReportResponse
import com.example.roadkill.api.ReportService
import com.example.roadkill.databinding.FragmentManagerHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ManagerHistoryFragment : Fragment() {
    private lateinit var binding: FragmentManagerHistoryBinding
    var accidentHistoryList: ArrayList<History> = ArrayList<History>()
    private lateinit var managerHistoryRVAdapter: ManagerHistoryRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManagerHistoryBinding.inflate(layoutInflater)

        managerHistoryRVAdapter = ManagerHistoryRVAdapter(accidentHistoryList)
        managerHistoryRVAdapter.setOnItemClickListener(object : ManagerHistoryRVAdapter.OnItemClickListener {
            override fun onItemClick(pos: Int) {
                val _id = accidentHistoryList[pos]._id
                val intent = Intent(activity, ManagerReceiptDetailActivity::class.java)
                intent.putExtra("_id", _id)
                startActivity(intent)
            }
        })

        val managerHistoryLinearLayoutManager = LinearLayoutManager(activity)
        managerHistoryLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvHistory.layoutManager = managerHistoryLinearLayoutManager
        binding.rvHistory.adapter = managerHistoryRVAdapter

        getManagerReportFun()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // View 객체 초기화 및 이벤트 처리 등을 수행
    }

    private fun getManagerReportFun(){
        //var email=binding.etSignupEmailleft.text.toString()+binding.etSignupEmailright.text.toString()
        ReportService.retrofitGetManagerReport().enqueue(object: Callback<List<ManagerReportResponse>> {
            override fun onResponse(
                call: Call<List<ManagerReportResponse>>,
                response: Response<List<ManagerReportResponse>>
            ) {
                if (response.isSuccessful) {
                    val managerReports = response.body()
                    if (managerReports != null) {
                        // JSON 배열을 반복하여 각각의 MyReportResponse 객체를 처리할 수 있습니다.
                        for (report in managerReports) {
                            if(!report.status) {
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
                                managerHistoryRVAdapter.notifyDataSetChanged();
                            }
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