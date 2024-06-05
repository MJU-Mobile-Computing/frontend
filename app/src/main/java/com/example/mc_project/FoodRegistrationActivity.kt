package com.example.mc_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.databinding.ActivityFoodRegistrationBinding

class FoodRegistrationActivity : BaseActivity() {

    private lateinit var binding: ActivityFoodRegistrationBinding
    private var totalCalories = 0
    private var totalCarbs = 0
    private var totalProteins = 0
    private var totalFats = 0
    private val maxCalories = 2000 // 하루 권장 섭취 칼로리
    private val FOOD_SEARCH_REQUEST_CODE = 100
    private var currentMealType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기 버튼 추가
        addBackButton()

        binding.btnAddBreakfast.setOnClickListener { openFoodSearchActivity("breakfast") }
        binding.btnAddLunch.setOnClickListener { openFoodSearchActivity("lunch") }
        binding.btnAddDinner.setOnClickListener { openFoodSearchActivity("dinner") }
        binding.btnAddSnack.setOnClickListener { openFoodSearchActivity("snack") }

        binding.btnDone.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("totalCalories", totalCalories)
            resultIntent.putExtra("totalCarbs", totalCarbs)
            resultIntent.putExtra("totalProteins", totalProteins)
            resultIntent.putExtra("totalFats", totalFats)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FOOD_SEARCH_REQUEST_CODE && resultCode == RESULT_OK) {
            val selectedFood = data?.getStringExtra("selectedFood")
            val calories = data?.getIntExtra("calories", 0) ?: 0
            Log.d("FoodRegistration", "Selected Food: $selectedFood, Calories: $calories")
            selectedFood?.let {
                displaySelectedFood(it, calories)
                addMealCalories(calories)
            }
        }
    }

    private fun openFoodSearchActivity(mealType: String) {
        currentMealType = mealType
        val intent = Intent(this, FoodSearchActivity::class.java)
        startActivityForResult(intent, FOOD_SEARCH_REQUEST_CODE)
    }

    private fun displaySelectedFood(selectedFood: String, calories: Int) {
        when (currentMealType) {
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
        // 총 칼로리를 표시하는 TextView를 업데이트
        binding.tvTotalCalories.text = "총 칼로리: $totalCalories kcal"
    }
}
