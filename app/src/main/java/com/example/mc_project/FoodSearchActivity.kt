package com.example.mc_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.databinding.ActivityFoodSearchBinding
import com.example.mc_project.models.ApiResponse
import com.example.mc_project.models.SearchFoodResponse
import com.google.gson.JsonObject
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

        // GPT 질문 버튼에 대한 클릭 리스너 설정
        binding.btnAskGPT.setOnClickListener {
            // ChatGPTActivity로 화면 전환을 위한 Intent 생성
            val chatGPTIntent = Intent(this@FoodSearchActivity, ChatGPTActivity::class.java)
            startActivity(chatGPTIntent)
        }
    }

    private fun searchFood(query: String) {
        val searchResultsLayout = binding.searchResultsLayout
        searchResultsLayout.removeAllViews()

        RetrofitInstance.api.searchFoods(query).enqueue(object : Callback<SearchFoodResponse> {
            override fun onResponse(call: Call<SearchFoodResponse>, response: Response<SearchFoodResponse>) {
                if (response.isSuccessful) {
                    val foods = response.body()?.data?.foods ?: emptyList()
                    for (food in foods) {
                        val foodView = LayoutInflater.from(this@FoodSearchActivity).inflate(R.layout.food_item, searchResultsLayout, false)
                        val tvFoodName: TextView = foodView.findViewById(R.id.tvFoodName)
                        val tvFoodCalories: TextView = foodView.findViewById(R.id.tvFoodCalories)
                        val tvFoodCarbohydrates: TextView = foodView.findViewById(R.id.tvFoodCarbohydrates)
                        val tvFoodProtein: TextView = foodView.findViewById(R.id.tvFoodProtein)
                        val tvFoodFat: TextView = foodView.findViewById(R.id.tvFoodFat)
                        val tvBookmarkStatus: TextView = foodView.findViewById(R.id.tvBookmarkStatus)
                        val btnBookmark: Button = foodView.findViewById(R.id.btnBookmark)
                        val btnDeleteBookmark: Button = foodView.findViewById(R.id.btnDeleteBookmark)

                        tvFoodName.text = food.foodName
                        tvFoodCalories.text = "Calories: ${food.calories}"
                        tvFoodCarbohydrates.text = "Carbohydrates: ${food.carbohydrates}"
                        tvFoodProtein.text = "Protein: ${food.protein}"
                        tvFoodFat.text = "Fat: ${food.fat}"

                        // Set bookmark status
                        tvBookmarkStatus.text = if (food.bookmarked) "✔️" else "✖️"

                        foodView.setOnClickListener {
                            // FoodSearchActivity에서 Intent로 데이터를 전달할 때
                            val selectedFoodIntent = Intent()
                            selectedFoodIntent.putExtra("selectedFood", food.foodName)
                            selectedFoodIntent.putExtra("calories", food.calories.toInt())
                            selectedFoodIntent.putExtra("carbohydrates", food.carbohydrates)
                            selectedFoodIntent.putExtra("proteins", food.protein)
                            selectedFoodIntent.putExtra("fat", food.fat)
                            setResult(RESULT_OK, selectedFoodIntent)
                            finish()
                        }

                        btnBookmark.setOnClickListener {
                            bookmarkFood(food.id)
                        }

                        btnDeleteBookmark.setOnClickListener {
                            deleteBookmark(food.id)
                        }

                        searchResultsLayout.addView(foodView)
                    }
                }
            }

            override fun onFailure(call: Call<SearchFoodResponse>, t: Throwable) {
                // API 호출 실패 처리
                showToast("Failed to retrieve food data")
            }
        })
    }

    private fun bookmarkFood(foodId: Int) {
        val jsonObject = JsonObject().apply {
            addProperty("foodId", foodId)
        }

        RetrofitInstance.api.bookmarkFood(jsonObject).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    showToast("Bookmark added successfully")
                } else {
                    showToast("Failed to add bookmark")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                showToast("Failed to add bookmark")
            }
        })
    }

    private fun deleteBookmark(foodId: Int) {
        RetrofitInstance.api.deleteBookmark(foodId).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    showToast("Bookmark deleted successfully")
                } else {
                    showToast("Failed to delete bookmark")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                showToast("Failed to delete bookmark")
            }
        })
    }

    // Toast 메시지를 띄우는 함수
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
