package com.example.roadkill

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roadkill.api.ReportService
import com.example.roadkill.databinding.ActivityUserReportBinding
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UserReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserReportBinding
    var reportImageList: ArrayList<Uri?> = ArrayList<Uri?>()
    lateinit var reportImageRVAdapter: ReportImageRVAdapter
    private var lat: Double = 0.0
    private var lng: Double = 0.0
   // private lateinit var species: String

    lateinit var filePath: String
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>
    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 123 // 원하는 요청 코드
    }
    private lateinit var photoURI: Uri

    // 이전 액티비티에서 카메라를 선택할 때
    private val REQUEST_CAMERA = 1

    // 이전 액티비티에서 갤러리를 선택할 때
    private val REQUEST_GALLERY = 2


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

        if(intent.getStringExtra("imageMethod") == "camera"){
            getImageFromCamera()
        }

        else if(intent.getStringExtra("imageMethod") == "gallery") {
            getImageFromGallery()
        }

        binding.tvAgain.setOnClickListener {
            val intent = Intent(applicationContext, CameraActivity::class.java)
            startActivity(intent)
        }

        //요청하기 버튼
        binding.tvBtnOk.setOnClickListener {
            if(reportImageList.size == 0)
                Toast.makeText(this, "이미지를 선택해주세요", Toast.LENGTH_SHORT).show()
            else {
                postReportFun()
                Toast.makeText(applicationContext, "요청이 접수되었습니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, Call911Activity::class.java)
                startActivity(intent)
            }
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
//        val fusedLocationProviderClient =
//            LocationServices.getFusedLocationProviderClient(this)
//
//        fusedLocationProviderClient.lastLocation
//            .addOnSuccessListener { success: Location? ->
//                success?.let { location ->
//                    textView.text =
//                        "${location.latitude}, ${location.longitude}"
//                    lat = location.latitude
//                    lng = location.longitude
//                }
//            }
//            .addOnFailureListener { fail ->
//                textView.text = fail.localizedMessage
//            }
        lat = MyApplication.prefs.getString("lat","").toDouble()
        lng = MyApplication.prefs.getString("lng","").toDouble()
        textView.text = "${lat}, ${lng}"

    }

    private fun getTime(textView: TextView){
//        val currentTime : Long = System.currentTimeMillis() // ms로 반환
//        val dataFormat5 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
//        textView.text = dataFormat5.format(currentTime)
        val dateInfo = MyApplication.prefs.getString("dateInfo", "")
        textView.text = dateInfo
    }

    private fun getImageFromCamera(){
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                Log.d("INFO", "RESULT_OK")
                val option = BitmapFactory.Options()
                option.inSampleSize = 10
                val bitmap = BitmapFactory.decodeFile(filePath, option)
            }
            else
                Log.d("INFO", "불러오기 실패")
        }

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(
            "JPEG_${timeStamp}_", ".jpg", storageDir
        )
        filePath = file.absolutePath
        photoURI = createImageFile()

        if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없을 경우 권한 요청
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            //  권한이 이미 부여되었을 경우 카메라 앱 호출 등 필요한 작업 수행
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            // resultLauncher.launch(cameraIntent)
            startActivityForResult(cameraIntent, REQUEST_CAMERA)
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여된 경우 필요한 작업 수행
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                resultLauncher.launch(intent)
            } else {
                // 권한이 거부된 경우 사용자에게 알림 표시 또는 다른 대체 작업 수행
            }
        }
    }

    private fun createImageFile(): Uri {
        val now = SimpleDateFormat("yyMMdd_HHmmss").format(Date())
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)!!
    }

    private fun getImageFromGallery(){
        //갤러리에서 이미지 가져오기
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(galleryIntent, REQUEST_GALLERY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CAMERA ->{
                var imageUri : Uri? = photoURI
                reportImageList.add(imageUri);

                reportImageRVAdapter = ReportImageRVAdapter(reportImageList, this);
                binding.rvAccidentImage.adapter = reportImageRVAdapter
                val reportImageLinearLayoutManager = LinearLayoutManager(this)
                reportImageLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                binding.rvAccidentImage.layoutManager = reportImageLinearLayoutManager
            }
            REQUEST_GALLERY -> {
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

    private fun postReportFun() {
        val img: Uri = reportImageList[0]!!
        val lat = lat
        val lng = lng

        val apiService = ApiClient.create(ReportService::class.java)

        // 이미지 파일 생성
        val filePath = getRealPathFromUri(this, img)
        Log.d("파일경로", filePath.toString())
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/*".toMediaType())
        val imagePart = MultipartBody.Part.createFormData("img", file.name, requestFile)

        // 네트워크 요청 및 응답 처리
        val call = apiService.postReport(
            img = imagePart,
            lat = lat,
            lng = lng,
            species = "동물종 분석 중입니다",
            cause = binding.etAccidentCauseInfo.text.toString(),
            otherInfoByUser = binding.etAccidentOtherInfo.text.toString(),
            status = false,
            accidentTime = binding.tvAccidentDateInfo.text.toString(),
            MyApplication.prefs.getString("id", "no id")
        )
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    println("신고 성공: $responseData")
                } else {
                    val errorBody = response.errorBody()?.string()
                    println("신고 실패: $errorBody")
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("TAG", "실패원인: $t")
            }
        })
    }

    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            filePath = it.getString(columnIndex)
        }
        return filePath
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
