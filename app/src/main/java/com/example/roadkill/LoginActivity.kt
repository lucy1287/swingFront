package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.api.LoginRequest
import com.example.roadkill.api.LoginService
import com.example.roadkill.api.NearmissRequest
import com.example.roadkill.api.NearmissService
import com.example.roadkill.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            val id = binding.etId.text.toString()
            val password = binding.etPassword.text .toString()

            if(id == "10000" && password == "1234") {
                Toast.makeText(this, "사용자로 로그인 되었습니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
            if(id == "20000" && password == "1234") {
                Toast.makeText(this, "관리자로 로그인 되었습니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, ManagerMainActivity::class.java)
                startActivity(intent)
            }
            else if(id == null){
                Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else if(password == null){
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else{
                postReportFun()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.tvSignup.setOnClickListener{
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun postReportFun() {
        var email = binding.etId.text.toString()
        var pwd = binding.etPassword.text.toString()
        var loginJson = LoginRequest(email, pwd)
        Log.d("dsds", loginJson.toString())

        LoginService.retrofitPostLoginReport(loginJson)
            .enqueue(object : Callback<String> { // 응답 타입을 String으로 지정
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        println("로그인 성공: $responseData")
                        Log.d("id1", responseData.toString().trim('"'))
                        MyApplication.prefs.setString("id", responseData.toString().trim('"'))
                        Log.d("id2", MyApplication.prefs.getString("id", "no id"))
                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("로그인 실패: $errorBody")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("TAG", "실패원인: $t")
                }
            })
    }
}