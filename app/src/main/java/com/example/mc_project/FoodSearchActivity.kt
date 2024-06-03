package com.example.mc_project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodSearchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_search)


        addBackButton()

        val etSearchFood: EditText = findViewById(R.id.etSearchFood)
        val btnSearchFood: Button = findViewById(R.id.btnSearchFood)
        val btnAskGPT: Button = findViewById(R.id.btnAskGPT)

        btnSearchFood.setOnClickListener { searchFood(etSearchFood.text.toString()) }
        btnAskGPT.setOnClickListener { navigateToChatGPT() }
    }

    private fun searchFood(query: String) {
        val searchResultsLayout: LinearLayout = findViewById(R.id.searchResultsLayout)
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

                        searchResultsLayout.addView(foodView)
                    }
                }
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                // Handle API call failure
            }
        })
    }

    private fun navigateToChatGPT() {
        val intent = Intent(this, ChatGPTActivity::class.java)
        startActivity(intent)
    }
}
