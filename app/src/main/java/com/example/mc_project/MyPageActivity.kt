package com.example.mc_project

import android.content.Intent
import android.os.Bundle
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

        // 프로필 및 목표 정보 설정
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
    }

    private fun fetchMyPageData() {
        RetrofitInstance.api.getMyPageData().enqueue(object : Callback<MyPageResponse> {
            override fun onResponse(call: Call<MyPageResponse>, response: Response<MyPageResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val data = it.data
                        binding.profileName.text = "${data.lastname} ${data.firstname}"
                        binding.profileAge.text = "${calculateAge(data.birthdate)} 세"
                        binding.profileHeight.text = "${data.height} cm"
                        binding.textViewGoal.text = "목표: ${data.goal}"
                        binding.textViewGoalWeight.text = "목표 몸무게: ${data.goalWeight} kg"
                        binding.textViewGoalCalorie.text = "하루 목표 권장 칼로리: ${calculateRecommendedCalories(data)} kcal"
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

    private fun calculateAge(birthdate: String): Int {
        val birthYear = birthdate.split("-")[0].toInt()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return currentYear - birthYear
    }

    private fun calculateRecommendedCalories(data: MyPageData): Double {
        val activityMultiplier = when (data.amountOfActivity) {
            "적음" -> 25.0
            "보통" -> 30.0
            "많음" -> 35.0
            else -> 0.0
        }
        var recommendedCalories = data.weight.replace("kg", "").toDoubleOrNull() ?: 0.0 * activityMultiplier
        recommendedCalories = when (data.goal) {
            "체중 감소" -> recommendedCalories - 150
            "체중 유지" -> recommendedCalories
            "체중 증량" -> recommendedCalories + 150
            else -> 0.0
        }
        return recommendedCalories
    }

    private fun calculateProgress(currentWeight: String, goalWeight: String): Int {
        val current = currentWeight.replace("kg", "").toDoubleOrNull() ?: 0.0
        val goal = goalWeight.replace("kg", "").toDoubleOrNull() ?: 0.0
        return if (current > 0) ((goal / current) * 100).toInt() else 0
    }
}
