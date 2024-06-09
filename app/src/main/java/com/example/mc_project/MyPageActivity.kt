package com.example.mc_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.databinding.ActivityMyPageBinding
import com.example.mc_project.models.MyPageData
import com.example.mc_project.models.MyPageResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MyPageActivity : BaseActivity() {
    lateinit var binding: ActivityMyPageBinding
    private val REQUEST_CODE_EDIT_PROFILE = 1
    private val REQUEST_CODE_EDIT_GOALS = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addBackButton()
        fetchMyPageData()

        binding.profile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE)
        }

        binding.goals.setOnClickListener {
            val intent = Intent(this, EditGoalsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_EDIT_GOALS)
        }

        binding.shareButton.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Check out this amazing app: [앱 이름]!\nYou can download it from [앱 다운로드 링크].")
                type = "text/plain"
            }
            val shareTitle = resources.getString(R.string.share_title)
            startActivity(Intent.createChooser(shareIntent, shareTitle))
        }

        // SharedData의 remainingCalories를 렌더링
        binding.textViewGoalCalorie.text = "하루 목표 권장 칼로리: ${SharedData.remainingCalories.toInt()} kcal"
    }

    private fun calculateAndSaveGoalCalories(data: MyPageData): Int {
        val goalCalorie = calculateRecommendedCalories(data)
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("goalCalorie", goalCalorie)
            apply()
        }
        SharedData.remainingCalories = goalCalorie.toDouble() // SharedData에 저장
        Log.d("MyPageActivity", "Saved goalCalorie: $goalCalorie") // 로그 추가
        return goalCalorie
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            data?.extras?.let { bundle ->
                if (requestCode == REQUEST_CODE_EDIT_PROFILE) {
                    val lastName = bundle.getString("lastName")
                    val firstName = bundle.getString("firstName")
                    val gender = bundle.getString("gender")
                    val dob = bundle.getString("dob")
                    val height = bundle.getString("height")

                    updateMyPageData(MyPageData(firstName, lastName, gender, dob, height, null, null, null, null, null))
                } else if (requestCode == REQUEST_CODE_EDIT_GOALS) {
                    val goal = bundle.getString("goal")
                    val currentWeight = bundle.getString("currentWeight")
                    val goalWeight = bundle.getString("goalWeight")
                    val activityLevel = bundle.getString("activityLevel")
                    val goalSteps = bundle.getString("goalSteps")

                    updateMyPageData(MyPageData(null, null, null, null, null, goal, currentWeight, activityLevel, goalWeight, goalSteps))
                }
            }
        }
    }

    private fun fetchMyPageData() {
        RetrofitInstance.api.getMyPageData().enqueue(object : Callback<MyPageResponse> {
            override fun onResponse(call: Call<MyPageResponse>, response: Response<MyPageResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val data = it.data
                        binding.profileName.text = "${data.lastname} ${data.firstname}"
                        binding.profileHeight.text = "${data.height} cm"
                        binding.textViewGoal.text = "목표: ${data.goal}"
                        binding.textViewGoalWeight.text = "목표 몸무게: ${data.goalWeight} kg"
                        binding.textViewGoalCalorie.text = "하루 목표 권장 칼로리: ${SharedData.remainingCalories.toInt()} kcal"
                        binding.textViewGoalSteps.text = "걸음 목표: ${data.goalSteps}"
                        binding.progressBar.progress = calculateProgress(data.weight, data.goalWeight)
                        binding.TextViewPrograss.text = "${calculateProgress(data.weight, data.goalWeight)}%"
                    }
                }
            }

            override fun onFailure(call: Call<MyPageResponse>, t: Throwable) {
                // Handle API call failure
            }
        })
    }

    private fun updateMyPageData(data: MyPageData) {
        RetrofitInstance.api.updateMyPageData(data).enqueue(object : Callback<MyPageResponse> {
            override fun onResponse(call: Call<MyPageResponse>, response: Response<MyPageResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MyPageActivity, "정보가 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
                    val goalCalories = calculateAndSaveGoalCalories(data) // 목표 칼로리를 계산하고 저장
                    SharedData.remainingCalories = goalCalories.toDouble() // SharedData에 저장
                    fetchMyPageData() // 업데이트된 정보 다시 불러오기
                }
            }

            override fun onFailure(call: Call<MyPageResponse>, t: Throwable) {
                Toast.makeText(this@MyPageActivity, "정보 업데이트에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun calculateAge(birthdate: String): Int {
        val birthYear = birthdate.split("-")[0].toInt()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return currentYear - birthYear
    }

    private fun calculateRecommendedCalories(data: MyPageData? = null): Int {
        // data가 null인 경우, 화면에서 가져온 데이터를 사용합니다.
        val myPageData = data ?: MyPageData(
            binding.profileName.text.toString(),
            "",
            "",
            "",
            binding.profileHeight.text.toString().replace(" cm", ""),
            binding.textViewGoal.text.toString(),
            binding.textViewGoalWeight.text.toString(),
            "",
            "",
            binding.textViewGoalSteps.text.toString()
        )

        val activityMultiplier = when (myPageData.amountOfActivity) {
            "적음" -> 25.0
            "보통" -> 30.0
            "많음" -> 35.0
            else -> 0.0
        }
        var recommendedCalories = (myPageData.weight?.replace("kg", "")?.toDoubleOrNull() ?: 0.0) * activityMultiplier
        recommendedCalories = when (myPageData.goal) {
            "체중 감소" -> recommendedCalories - 150
            "체중 유지" -> recommendedCalories
            "체중 증량" -> recommendedCalories + 150
            else -> 0.0
        }
        return recommendedCalories.toInt()
    }

    private fun calculateProgress(currentWeight: String?, goalWeight: String?): Int {
        val current = currentWeight?.replace("kg", "")?.toDoubleOrNull() ?: 0.0
        val goal = goalWeight?.replace("kg", "")?.toDoubleOrNull() ?: 0.0
        return if (current > 0) ((goal / current) * 100).toInt() else 0
    }
}
