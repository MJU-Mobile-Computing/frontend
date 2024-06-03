package com.example.mc_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mc_project.databinding.ActivityFoodRegistrationBinding

class FoodRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodRegistrationBinding
    private var totalCalories = 0
    private val maxCalories = 2000 // 하루 권장 섭취 칼로리
    private val FOOD_SEARCH_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기 버튼 추가
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnAddBreakfast.setOnClickListener { openFoodSearchActivity("breakfast") }
        binding.btnAddLunch.setOnClickListener { openFoodSearchActivity("lunch") }
        binding.btnAddDinner.setOnClickListener { openFoodSearchActivity("dinner") }
        binding.btnAddSnack.setOnClickListener { openFoodSearchActivity("snack") }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FOOD_SEARCH_REQUEST_CODE && resultCode == RESULT_OK) {
            val selectedFood = data?.getStringExtra("selectedFood")
            val calories = data?.getIntExtra("calories", 0) ?: 0
            selectedFood?.let { displaySelectedFood(it, calories) }
            addMealCalories(calories)
        }
    }

    private fun openFoodSearchActivity(mealType: String) {
        val intent = Intent(this, FoodSearchActivity::class.java)
        intent.putExtra("mealType", mealType)
        startActivityForResult(intent, FOOD_SEARCH_REQUEST_CODE)
    }

    private fun displaySelectedFood(selectedFood: String, calories: Int) {
        when (selectedFood) {
            "breakfast" -> {
                binding.tvSelectedBreakfastFood.text = selectedFood
                binding.tvSelectedBreakfastCalories.text = "칼로리: $calories kcal"
            }
            "lunch" -> {
                binding.tvSelectedLunchFood.text = selectedFood
                binding.tvSelectedLunchCalories.text = "칼로리: $calories kcal"
            }
            "dinner" -> {
                binding.tvSelectedDinnerFood.text = selectedFood
                binding.tvSelectedDinnerCalories.text = "칼로리: $calories kcal"
            }
            "snack" -> {
                binding.tvSelectedSnackFood.text = selectedFood
                binding.tvSelectedSnackCalories.text = "칼로리: $calories kcal"
            }
        }
    }

    private fun addMealCalories(calories: Int) {
        totalCalories += calories
        updateActivityRecommendation()
    }

    private fun updateActivityRecommendation() {
        val excessCalories = totalCalories - maxCalories
        if (excessCalories > 0) {
            binding.tvActivityTitle.text = "추천 활동"
            binding.tvActivityDescription.text = "추가된 칼로리만큼 활동으로 소모할 수 있습니다."

            val activityList = listOf(
                ActivityRecommendation("산책", R.drawable.ic_walk, (excessCalories / 5).toString() + "분"),
                ActivityRecommendation("달리기", R.drawable.ic_run, (excessCalories / 10).toString() + "분")
            )

            binding.activityRecommendation.removeAllViews()

            for (activity in activityList) {
                val activityView = LayoutInflater.from(this).inflate(R.layout.activity_item, binding.activityRecommendation, false)
                val imgActivity: ImageView = activityView.findViewById(R.id.imgActivity)
                val tvActivityName: TextView = activityView.findViewById(R.id.tvActivityName)
                val tvActivityDuration: TextView = activityView.findViewById(R.id.tvActivityDuration)

                imgActivity.setImageDrawable(ContextCompat.getDrawable(this, activity.iconRes))
                tvActivityName.text = activity.name
                tvActivityDuration.text = activity.duration

                binding.activityRecommendation.addView(activityView)
            }
        }
    }

    data class ActivityRecommendation(val name: String, val iconRes: Int, val duration: String)
}
