package com.example.roadkill

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.roadkill.databinding.ActivityTimeDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class TimeDetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityTimeDetailBinding
    private var dateString = ""
    private var timeString = ""

    private var selectedYear: Int = 0
    private var selectedMonth: Int = 0
    private var selectedDayOfMonth: Int = 0
    private var selectedHourOfDay: Int = 0
    private var selectedMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvDate.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                selectedYear = year
                selectedMonth = month
                selectedDayOfMonth = dayOfMonth
                dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
                binding.tvDate.text = dateString

                if(binding.tvTime.text != "시간 선택") {
                    binding.tvBtn2.setBackgroundResource(R.drawable.green_round_background)
                    binding.tvBtn2.isEnabled = true
                }
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.tvTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                selectedHourOfDay = hourOfDay
                selectedMinute = minute
                timeString = "${hourOfDay}시 ${minute}분"
                binding.tvTime.text = timeString

                if(binding.tvDate.text != "날짜 선택") {
                    binding.tvBtn2.setBackgroundResource(R.drawable.green_round_background)
                    binding.tvBtn2.isEnabled = true
                }
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),true).show()
        }

        binding.tvBtn2.setOnClickListener{
            // Calendar 객체를 사용하여 연도, 월, 일, 시간 및 분 값을 설정
            val calendar = Calendar.getInstance()
            calendar.set(selectedYear, selectedMonth, selectedDayOfMonth, selectedHourOfDay, selectedMinute)

            // SimpleDateFormat을 사용하여 원하는 날짜 및 시간 형식으로 변환
            val dateFormat5 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateInfo = dateFormat5.format(calendar.time)
            MyApplication.prefs.setString("dateInfo", dateInfo)

            val intent = Intent(applicationContext, NaverMapActivity::class.java)
            startActivity(intent)
        }
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