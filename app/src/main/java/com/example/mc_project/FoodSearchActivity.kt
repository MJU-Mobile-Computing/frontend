package com.example.mc_project

import android.app.Activity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView

class FoodSearchActivity : BaseActivity() {

    private val foodList = listOf(
        "Apple - 95 kcal",
        "Banana - 105 kcal",
        "Orange - 62 kcal",
        "Sandwich - 300 kcal",
        "Pizza - 266 kcal",
        // Add more food items here
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_search)

        val editTextSearch: EditText = findViewById(R.id.editTextSearch)
        val listViewSearchResults: ListView = findViewById(R.id.listViewSearchResults)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, foodList)
        listViewSearchResults.adapter = adapter

        listViewSearchResults.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedFood = adapter.getItem(position)
            val resultIntent = intent
            resultIntent.putExtra("selectedFood", selectedFood)
            val calories = selectedFood?.split(" - ")?.get(1)?.split(" ")?.get(0)?.toIntOrNull() ?: 0
            resultIntent.putExtra("calories", calories)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
