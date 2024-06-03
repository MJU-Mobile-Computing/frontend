package com.example.mc_project
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.databinding.ActivityMyPageBinding
import android.content.Intent

class MyPageActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyPageBinding
    private val REQUEST_CODE_EDIT_PROFILE = 1
    private val REQUEST_CODE_EDIT_GOALS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 프로필 정보 설정
        binding.profile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE)
        }

//        // 목표 설정
        binding.goals.setOnClickListener {
            val intent = Intent(this, EditGoalsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE)
        }
        //        // 추천 설정
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EDIT_PROFILE && resultCode == Activity.RESULT_OK) {
            data?.extras?.let { bundle ->
                val lastName = bundle.getString("lastName")
                val firstName = bundle.getString("firstName")
                val gender = bundle.getString("gender")
                val age = bundle.getString("age")
                val height = bundle.getString("height")

                binding.profileName.text = "$lastName$firstName"
                binding.profileAge.text = age +"세"
                binding.profileHeight.text = height +"cm"
            }
        }

        if (requestCode == REQUEST_CODE_EDIT_GOALS && resultCode == Activity.RESULT_OK) {
            data?.extras?.let { bundle ->
                val goal = bundle.getString("goal")
                val currentWeight = bundle.getString("currentWeight")?.toDoubleOrNull()
                val goalWeight = bundle.getString("goalWeight")?.toDoubleOrNull()
                val activityLevel = bundle.getString("activityLevel")
                val goalSteps = bundle.getString("goalSteps")

                //일일 권장 칼로리 계산
                var recommendedCalories = when (activityLevel) {
                    "적음" -> currentWeight?.times(25) ?: 0.0 // Low 활동 수준
                    "보통" -> currentWeight?.times(30) ?: 0.0 // Moderate 활동 수준
                    "많음" -> currentWeight?.times(35) ?: 0.0 // High 활동 수준
                    else -> 0.0 // 기본 값
                }
                //목표에 따른 권장 칼로리 수정
                recommendedCalories = when (goal) {
                    "체중 감소" -> recommendedCalories - 150
                    "체중 유지" -> recommendedCalories
                    "체중 증량" -> recommendedCalories + 150
                    else -> 0.0 // 기본 값
                }

                //성과 시각화
                val progress = if (currentWeight != null && goalWeight != null && currentWeight > 0) {
                    ((goalWeight / currentWeight) * 100).toInt()
                } else {
                    0 // 기본 값
                }

                // 데이터를 UI에 반영
                binding.textViewGoal.text = "목표: $goal"
                binding.textViewGoalWeight.text = "목표 몸무게: $goalWeight"
                binding.textViewGoalCalorie.text = "하루 목표 권장 칼로리: ${recommendedCalories}kcal"
                binding.textViewGoalSteps.text = "걸음 목표: $goalSteps"
                binding.progressBar.progress = progress
                binding.TextViewPrograss.text = "$progress%"
            }
        }
    }
}