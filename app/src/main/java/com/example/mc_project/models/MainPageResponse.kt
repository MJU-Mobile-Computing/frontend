package com.example.mc_project.models

import com.google.gson.annotations.SerializedName

data class MainPageResponse(
    @SerializedName("data") val data: Data,
    @SerializedName("transaction_time") val transactionTime: String,
    @SerializedName("status") val status: String,
    @SerializedName("description") val description: String?,
    @SerializedName("statusCode") val statusCode: Int
)

data class Data(
    @SerializedName("totalCalories") val totalCalories: Double,
    @SerializedName("totalCarbohydrate") val totalCarbohydrate: Double,
    @SerializedName("totalProteins") val totalProteins: Double,
    @SerializedName("totalFat") val totalFat: Double,
    @SerializedName("totalExerciseTime") val totalBurnedCalories: Long
)
