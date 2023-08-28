package com.example.roadkill

import RegisterRequest
import RegisterResponse
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.api.RegisterService
import com.example.roadkill.databinding.ActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var userSelected: Boolean = false
    private var managerSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val itemList = listOf("역할을 선택하세요", "사용자", "관리자")
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, itemList)
        binding.spinnerRole.adapter = adapter

        binding.spinnerRole.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 1){
                    userSelected = true
                    managerSelected = false
                }
                else if(position == 2) {
                    userSelected = false
                    managerSelected = true
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.btnSignUp.setOnClickListener{
            if(!userSelected && managerSelected) {
                Toast.makeText(
                    this@SignUpActivity,
                    "관리자로 회원가입하려면 관리자 인증이 필요합니다",
                    Toast.LENGTH_SHORT
                ).show()
                postRegisterFun()

                val intent = Intent(applicationContext, ManagerCertificationActivity::class.java)
                startActivity(intent)
            }
            else if(userSelected && !managerSelected){
                postRegisterFun()
                Toast.makeText(
                    this@SignUpActivity,
                    "회원가입 되었습니다",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun postRegisterFun() {
            var name = binding.etId.text.toString()
            var email = binding.etEmail.text.toString()
            var pwd = binding.etPassword.text.toString()
            var memberType: Int = 0
            var registerJson = RegisterRequest(name, email, pwd, memberType)
            Log.d("dsds", registerJson.toString())

            RegisterService.retrofitPostRegister(registerJson)
            .enqueue(object : Callback<String> { // 응답 타입을 String으로 지정
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    println("회원가입 성공: $responseData")
                } else {
                    val errorBody = response.errorBody()?.string()
                    println("회원가입 실패: $errorBody")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("TAG", "실패원인: $t")
            }
        })
        }

}