package com.example.mc_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.mc_project.databinding.ActivityEditgoalsBinding
import com.example.mc_project.models.GoalCaloriesRequest
import com.example.mc_project.models.MessageResponse
import com.example.mc_project.models.MyPageGoalData
import com.example.mc_project.models.MyPageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditGoalsActivity : BaseActivity() {

    private lateinit var binding: ActivityEditgoalsBinding
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditgoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addBackButton()

        ArrayAdapter.createFromResource(
            this,
            R.array.mygoal_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGoal.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.activity_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerActivityLevel.adapter = adapter
        }

        setInitialValues()

        binding.buttonEdit.setOnClickListener {
            toggleEditMode()
        }
    }

    private fun setInitialValues() {
        binding.editTextCurrentWeight.setText("70")
        binding.editTextGoalWeight.setText("65")
        binding.editTextGoalSteps.setText("10000")
    }

    private fun toggleEditMode() {
        isEditing = !isEditing

        binding.editTextCurrentWeight.isEnabled = isEditing
        binding.editTextGoalWeight.isEnabled = isEditing
        binding.editTextGoalSteps.isEnabled = isEditing
        binding.spinnerGoal.isEnabled = isEditing
        binding.spinnerActivityLevel.isEnabled = isEditing

        if (isEditing) {
            binding.buttonEdit.text = "저장"
        } else {
            binding.buttonEdit.text = "수정"
            saveGoals()
        }
    }

    override fun onBackPressed() {
        saveGoals() // 뒤로가기 버튼을 누를 때도 목표를 저장
        super.onBackPressed()
    }

    private fun saveGoals() {
        val goal = binding.spinnerGoal.selectedItem.toString()
        val currentWeight = binding.editTextCurrentWeight.text.toString()
        val goalWeight = binding.editTextGoalWeight.text.toString()
        val activityLevel = binding.spinnerActivityLevel.selectedItem.toString()
        val goalSteps = binding.editTextGoalSteps.text.toString()

        if (currentWeight.toDoubleOrNull() == null || currentWeight.toDouble() <= 0) {
            Toast.makeText(this, "유효한 현재 몸무게를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (goalWeight.toDoubleOrNull() == null || goalWeight.toDouble() <= 0) {
            Toast.makeText(this, "유효한 목표 몸무게를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (goalSteps.toDoubleOrNull() == null || goalSteps.toDouble() <= 0) {
            Toast.makeText(this, "유효한 목표 걸음 수를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val myPageGoalData = MyPageGoalData(goal, currentWeight, goalWeight, goalSteps)

        // 목표 데이터를 업데이트
        RetrofitInstance.api.updateMyPageGoalData(myPageGoalData).enqueue(object : Callback<MyPageResponse> {
            override fun onResponse(call: Call<MyPageResponse>, response: Response<MyPageResponse>) {
                if (response.isSuccessful) {
                    // 목표 칼로리를 계산하여 갱신
                    val newGoalCalories = calculateRecommendedCalories(currentWeight.toDouble(), goalWeight.toDouble(), activityLevel)
                    setGoalCalories(newGoalCalories)
                } else {
                    Toast.makeText(this@EditGoalsActivity, "마이페이지 정보 수정 실패", Toast.LENGTH_SHORT).show()
                    Log.e("EditGoalsActivity", "API Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<MyPageResponse>, t: Throwable) {
                Toast.makeText(this@EditGoalsActivity, "마이페이지 정보 수정 오류", Toast.LENGTH_SHORT).show()
                Log.e("EditGoalsActivity", "API Failure", t)
            }
        })
    }

    private fun setGoalCalories(goalCalories: Float) {
        val request = GoalCaloriesRequest(goalCalories)
        RetrofitInstance.api.setGoalCalories(request).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if (response.isSuccessful) {
                    // SharedPreferences에 목표 칼로리 저장
                    val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putInt("goalCalorie", goalCalories.toInt())
                    editor.apply()

                    Toast.makeText(this@EditGoalsActivity, "목표 칼로리 설정 완료", Toast.LENGTH_SHORT).show()
                    navigateToMain(goalCalories)
                } else {
                    Toast.makeText(this@EditGoalsActivity, "목표 칼로리 설정 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Toast.makeText(this@EditGoalsActivity, "목표 칼로리 설정 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToMain(goalCalories: Float) {
        val intent = Intent(this@EditGoalsActivity, MainActivity::class.java)
        intent.putExtra("goalCalories", goalCalories)
        startActivity(intent)
        finish()
    }

    private fun calculateRecommendedCalories(currentWeight: Double, goalWeight: Double, activityLevel: String): Float {
        // Simple calculation for example purposes
        // You can replace this with the actual formula for calculating recommended calories
        return (currentWeight * 10 + goalWeight * 6.25).toFloat() + 700
    }

    private fun navigateToCalendar(goalCalories: Float) {
        val intent = Intent(this@EditGoalsActivity, CalendarActivity::class.java)
        intent.putExtra("goalCalories", goalCalories)
        startActivity(intent)
        finish()
    }
}
