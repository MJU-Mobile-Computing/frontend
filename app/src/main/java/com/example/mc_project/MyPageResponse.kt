package com.example.mc_project.models

import com.google.gson.annotations.SerializedName

data class MyPageResponse(
    @SerializedName("data") val data: MyPageData,
    @SerializedName("transaction_time") val transactionTime: String,
    @SerializedName("status") val status: String,
    @SerializedName("description") val description: String?,
    @SerializedName("statusCode") val statusCode: Int
)

data class MyPageData(
    @SerializedName("firstname") val firstname: String?,
    @SerializedName("lastname") val lastname: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("birthdate") val birthdate: String?,
    @SerializedName("height") val height: String?,
    @SerializedName("goal") val goal: String?,
    @SerializedName("weight") val weight: String?,
    @SerializedName("amountOfActivity") val amountOfActivity: String?,
    @SerializedName("goalWeight") val goalWeight: String?,
    @SerializedName("goalSteps") val goalSteps: String?
)

