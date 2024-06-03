package com.example.mc_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class FoodRegistrationActivity : AppCompatActivity() {

    private var totalCalories = 0
    private val maxCalories = 2000 // 하루 권장 섭취 칼로리

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_registration)




        val btnAddBreakfast: Button = findViewById(R.id.btnAddBreakfast)
        val btnAddLunch: Button = findViewById(R.id.btnAddLunch)
        val btnAddDinner: Button = findViewById(R.id.btnAddDinner)
        val btnAddSnack: Button = findViewById(R.id.btnAddSnack)

        btnAddBreakfast.setOnClickListener { navigateToFoodSearch() }
        btnAddLunch.setOnClickListener { navigateToFoodSearch() }
        btnAddDinner.setOnClickListener { navigateToFoodSearch() }
        btnAddSnack.setOnClickListener { navigateToFoodSearch() }
    }

    private fun navigateToFoodSearch() {
        val intent = Intent(this, FoodSearchActivity::class.java)
        startActivity(intent)
    }

    private fun addMealCalories(calories: Int) {
        totalCalories += calories
        updateActivityRecommendation()
    }

    private fun updateActivityRecommendation() {
        val excessCalories = totalCalories - maxCalories
        if (excessCalories > 0) {
            val tvActivityTitle: TextView = findViewById(R.id.tvActivityTitle)
            val tvActivityDescription: TextView = findViewById(R.id.tvActivityDescription)

            tvActivityTitle.text = "추천 활동"
            tvActivityDescription.text = "추가된 칼로리만큼 활동으로 소모할 수 있습니다."

            val activityList = listOf(
                ActivityRecommendation("산책", R.drawable.ic_walk, (excessCalories / 5).toString() + "분"),
                ActivityRecommendation("달리기", R.drawable.ic_run, (excessCalories / 10).toString() + "분")
            )

            val activityRecommendationLayout: LinearLayout = findViewById(R.id.activityRecommendation)
            activityRecommendationLayout.removeAllViews()

            for (activity in activityList) {
                val activityView = LayoutInflater.from(this).inflate(R.layout.activity_item, activityRecommendationLayout, false)
                val imgActivity: ImageView = activityView.findViewById(R.id.imgActivity)
                val tvActivityName: TextView = activityView.findViewById(R.id.tvActivityName)
                val tvActivityDuration: TextView = activityView.findViewById(R.id.tvActivityDuration)

                imgActivity.setImageDrawable(ContextCompat.getDrawable(this, activity.iconRes))
                tvActivityName.text = activity.name
                tvActivityDuration.text = activity.duration

                activityRecommendationLayout.addView(activityView)
            }
        }
    }

    data class ActivityRecommendation(val name: String, val iconRes: Int, val duration: String)
}
