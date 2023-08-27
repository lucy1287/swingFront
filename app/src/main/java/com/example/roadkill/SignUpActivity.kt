package com.example.roadkill

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivitySignUpBinding

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
                val intent = Intent(applicationContext, ManagerCertificationActivity::class.java)
                startActivity(intent)
            }
            else if(userSelected && !managerSelected){
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}