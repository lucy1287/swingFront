package com.example.roadkill

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.roadkill.api.ManagerUpdateRequest
import com.example.roadkill.api.MyReportResponse
import com.example.roadkill.api.ReportService
import com.example.roadkill.databinding.ActivityManagerReceiptDetailBinding
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManagerReceiptDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManagerReceiptDetailBinding
    private var receiptImageList: ArrayList<Uri?> = ArrayList<Uri?>()
    private lateinit var receiptImageRVAdapter: ReceiptImageRVAdapter
    private lateinit var rid: String

    override fun onStart() {
        super.onStart()
        RequestPermissionsUtil(this).requestLocation() // 위치 권한 요청
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerReceiptDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //RequestPermissionsUtil(this).requestLocation()

        rid = intent.getStringExtra("rid").toString()
        if (rid != null) {
            getManagerDetailFun(rid)
        }

        binding.tvSpeciesChange.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_input, null)
            val inputEditText: EditText = dialogView.findViewById(R.id.inputEditText)

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("변경할 동물종을 입력하세요.")
                .setView(dialogView)
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    // 사용자가 "확인" 버튼을 클릭했을 때 처리할 코드
                    val managerInput = inputEditText.text.toString()
                    // 입력된 텍스트를 화면에 표시
                    binding.tvSpeciesResultInfo.text = managerInput
                    // 여기에서는 간단히 토스트 메시지로 출력합니다.
                  //  Toast.makeText(this, "입력된 텍스트: $userInput", Toast.LENGTH_SHORT).show()
                })
                .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                    // 사용자가 "취소" 버튼을 클릭했을 때 처리할 코드
                    dialog.dismiss()
                })
                .create()

            alertDialog.show()
        }
        //처리하기 버튼
        binding.tvBtnOk.setOnClickListener{
                postManagerUpdate(rid)
                Toast.makeText(applicationContext, "처리가 완료되었습니다", Toast.LENGTH_SHORT).show()
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
                            Glide.with(this@ManagerReceiptDetailActivity) // YourActivity를 현재 활성화된 액티비티로 변경해야 합니다.
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

    private fun postManagerUpdate(rid: String) {
        var managerUpdateJson = ManagerUpdateRequest(
            binding.tvSpeciesResultInfo.text.toString(),
            binding.tvAccidentCauseInfo.text.toString(), "", true)

        ReportService.retrofitPostManagerUpdate(managerUpdateJson, rid)
            .enqueue(object : Callback<String> { // 응답 타입을 String으로 지정
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        println("업데이트 성공: $responseData")

                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("업데이트 실패: $errorBody")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("TAG", "실패원인: $t")
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.manager_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        return when (item.itemId) {
            R.id.item1 -> {
                val intent = Intent(applicationContext, ManagerMainActivity::class.java)
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




