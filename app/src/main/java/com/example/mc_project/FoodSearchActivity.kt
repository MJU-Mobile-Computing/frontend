package com.example.mc_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.databinding.ActivityFoodSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearchFood.setOnClickListener { searchFood(binding.etSearchFood.text.toString()) }
    }

    private fun searchFood(query: String) {
        val searchResultsLayout = binding.searchResultsLayout
        searchResultsLayout.removeAllViews()

        RetrofitInstance.api.searchFoods(query).enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    val foods = response.body()?.data?.foods ?: emptyList()
                    for (food in foods) {
                        val foodView = LayoutInflater.from(this@FoodSearchActivity).inflate(R.layout.food_item, searchResultsLayout, false)
                        val tvFoodName: TextView = foodView.findViewById(R.id.tvFoodName)
                        val tvFoodCalories: TextView = foodView.findViewById(R.id.tvFoodCalories)

                        tvFoodName.text = food.foodName
                        tvFoodCalories.text = "${food.calories} 칼로리"

                        foodView.setOnClickListener {
                            // FoodSearchActivity에서 Intent로 데이터를 전달할 때
                            val selectedFoodIntent = Intent()
                            selectedFoodIntent.putExtra("selectedFood", food.foodName)
                            selectedFoodIntent.putExtra("calories", food.calories.toInt()) // Double을 Int로 변환하여 전달
                            setResult(RESULT_OK, selectedFoodIntent)
                            finish()

                        }

                        searchResultsLayout.addView(foodView)
                    }
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                // Handle API call failure
            }
        })
    }
}
