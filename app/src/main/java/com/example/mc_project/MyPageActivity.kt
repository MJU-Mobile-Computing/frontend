package com.example.mc_project
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.databinding.ActivityMyPageBinding
import android.content.Intent

class MyPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 프로필 정보 설정
        binding.profile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

//        // 성과 설정
//        binding.achievements.setOnClickListener {
////            startActivity(Intent(this, EditAchievementsActivity::class.java))
//        }
//
//        // 목표 설정
//        binding.goals.setOnClickListener {
////            startActivity(Intent(this, EditGoalsActivity::class.java))
//        }
//
//        // 추천 설정
//        binding.recommendation.setOnClickListener {
////            startActivity(Intent(this, RecommendationsActivity::class.java))
//        }

    }
}

