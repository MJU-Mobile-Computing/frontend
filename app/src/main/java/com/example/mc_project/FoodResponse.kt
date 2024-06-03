package com.example.mc_project

data class FoodResponse(
    val data: FoodData,
    val transaction_time: String,
    val status: String,
    val description: String?,
    val statusCode: Int
)

data class FoodData(
    val foods: List<Food>
)

data class Food(
    val id: Int,
    val foodName: String,
    val calories: Double,
    val carbohydrates: Double,
    val protein: Double,
    val fat: Double,
    val water: Double
)
