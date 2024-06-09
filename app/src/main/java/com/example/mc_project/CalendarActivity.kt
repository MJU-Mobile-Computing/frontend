package com.example.mc_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.databinding.ActivityCalendarBinding
import com.example.mc_project.models.GoalCaloriesRequest
import com.example.mc_project.models.MessageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarActivity : BaseActivity() {
    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addBackButton()

        // 기존 코드 생략...

        binding.btnSmt.setOnClickListener {
            saveDiary()
        }
    }

    // 다이어리 내용 저장
    private fun saveDiary() {
        val caloriesContent = binding.editTextCalories.text.toString()
        val weightContent = binding.editTextWeight.text.toString()
        val diaryContent = "$caloriesContent\n$weightContent"

        // 목표 칼로리 설정 API 호출
        val goalCalories = caloriesContent.toFloatOrNull()
        if (goalCalories != null) {
            setGoalCalories(goalCalories)
        }
    }

    // 목표 칼로리 설정 API 호출
    private fun setGoalCalories(goalCalories: Float) {
        val request = GoalCaloriesRequest(goalCalories)
        RetrofitInstance.api.setGoalCalories(request).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CalendarActivity, "목표 칼로리 설정 완료", Toast.LENGTH_SHORT).show()
                    Log.d("CalendarActivity", "API Response: 목표 칼로리 설정 완료")
                    // 성공적으로 설정되면 결과를 반환하고 액티비티 종료
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
