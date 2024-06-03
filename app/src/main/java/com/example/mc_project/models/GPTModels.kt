package com.example.mc_project.models

data class QuestionRequest(
    val question: String
)

data class GPTResponse(
    val data: String,
    val transaction_time: String,
    val status: String,
    val description: String?,
    val statusCode: Int
)
