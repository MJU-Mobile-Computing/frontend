package com.example.mc_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.databinding.ActivityFoodSearchBinding
import com.example.mc_project.models.SearchFoodResponse
import com.example.mc_project.models.SearchFood
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

                        tvFoodName.text = food.foodName
                        tvFoodCalories.text = "${food.calories} 칼로리"

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

                        searchResultsLayout.addView(foodView)
                    }
                }
            }

            override fun onFailure(call: Call<SearchFoodResponse>, t: Throwable) {
                // API 호출 실패 처리
            }
        })
    }

    // Toast 메시지를 띄우는 함수
    private fun showToast(message: String) {
        // 현재 액티비티의 context를 사용하여 Toast 메시지를 띄웁니다.
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
