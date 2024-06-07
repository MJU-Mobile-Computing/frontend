package com.example.mc_project.models

data class FoodRegistrationRequest(
    val calories: String,
    val carbohydrates: String,
    val proteins: String,
    val fat: String,
    val mealType: String
)

data class FoodRegistrationResponse(
    val data: FoodRegistrationData
)

data class FoodRegistrationData(
    val message: String
)
