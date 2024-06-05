package com.example.mc_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewSearchResults)

        recyclerView.layoutManager = LinearLayoutManager(this)

        btnSearchFood.setOnClickListener { searchFood(etSearchFood.text.toString(), recyclerView) }
        btnAskGPT.setOnClickListener { navigateToChatGPT() }
    }

    private fun searchFood(query: String, recyclerView: RecyclerView) {
        RetrofitInstance.api.searchFoods(query).enqueue(object : Callback<FoodResponse> {
            override fun onResponse(call: Call<FoodResponse>, response: Response<FoodResponse>) {
                if (response.isSuccessful) {
                    val foods = response.body()?.data?.foods ?: emptyList()
                    recyclerView.adapter = FoodAdapter(foods)
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
