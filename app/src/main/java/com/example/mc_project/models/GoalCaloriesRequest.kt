package com.example.mc_project.models

import com.google.gson.annotations.SerializedName

data class GoalCaloriesRequest(
    @SerializedName("goalCalories") val goalCalories: Float
)

data class MessageResponse(
    @SerializedName("data") val data: Data,
    @SerializedName("transaction_time") val transactionTime: String,
    @SerializedName("status") val status: String,
    @SerializedName("description") val description: String?,
    @SerializedName("statusCode") val statusCode: Int
)
