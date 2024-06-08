package com.example.mc_project.models

data class SearchFoodResponse(
    val data: SearchFoodData,
    val transaction_time: String,
    val status: String,
    val description: String?,
    val statusCode: Int
)

data class SearchFoodData(
    val foods: List<SearchFood>
)

data class SearchFood(
    val id: Int,
    val foodName: String,
    val calories: Double,
    val carbohydrates: Double,
    val protein: Double,
    val fat: Double,
    val water: Double,
    val bookmarked: Boolean
)
