package com.example.mc_project.models

import com.google.gson.annotations.SerializedName

data class MyPageGoalData(
    @SerializedName("goal") val goal: String,
    @SerializedName("weight") val weight: String,
    @SerializedName("goalWeight") val goalWeight: String,
    @SerializedName("goalSteps") val goalSteps: String
)
