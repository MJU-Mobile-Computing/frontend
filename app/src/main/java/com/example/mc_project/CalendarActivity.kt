package com.example.mc_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mc_project.databinding.ActivityCalendarBinding
import com.example.mc_project.models.GoalCaloriesRequest
import com.example.mc_project.models.MainPageResponse
import com.example.mc_project.models.MessageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : BaseActivity() {
    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addBackButton()

        // 서버에서 목표 칼로리 값을 가져옴
        fetchGoalCalories()

        binding.btnSmt.setOnClickListener {
            saveDiary()
        }
    }

    private fun fetchGoalCalories() {
        val currentDate = getCurrentDate()
        RetrofitInstance.api.getMainPageDataByDate(currentDate).enqueue(object : Callback<MainPageResponse> {
            override fun onResponse(call: Call<MainPageResponse>, response: Response<MainPageResponse>) {
                if (response.isSuccessful) {
                    val goalCalories = response.body()?.data?.goalCalories ?: 0f
                    binding.editTextCalories.setText(goalCalories.toString())
                } else {
                    Log.e("CalendarActivity", "Failed to fetch goal calories")
                }
            }

            override fun onFailure(call: Call<MainPageResponse>, t: Throwable) {
                Log.e("CalendarActivity", "API call failed", t)
            }
        })
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun saveDiary() {
        val caloriesContent = binding.editTextCalories.text.toString()
        val weightContent = binding.editTextWeight.text.toString()
        val diaryContent = "$caloriesContent\n$weightContent"

        val goalCalories = caloriesContent.toFloatOrNull()
        if (goalCalories != null) {
            setGoalCalories(goalCalories)
        }
    }

    private fun setGoalCalories(goalCalories: Float) {
        val request = GoalCaloriesRequest(goalCalories)
        RetrofitInstance.api.setGoalCalories(request).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CalendarActivity, "목표 칼로리 설정 완료", Toast.LENGTH_SHORT).show()
                    Log.d("CalendarActivity", "API Response: 목표 칼로리 설정 완료")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@CalendarActivity, "목표 칼로리 설정 실패", Toast.LENGTH_SHORT).show()
                    Log.e("CalendarActivity", "API Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Toast.makeText(this@CalendarActivity, "목표 칼로리 설정 오류", Toast.LENGTH_SHORT).show()
                Log.e("CalendarActivity", "API Failure", t)
            }
        })
    }
}
