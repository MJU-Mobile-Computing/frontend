package com.example.mc_project

import com.example.mc_project.models.QuestionRequest
import com.example.mc_project.models.GPTResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FoodApiService {
    @GET("foods")
    fun searchFoods(@Query("foodName") foodName: String): Call<FoodResponse>

    @POST("api/v1/chat-gpt")
    fun askGPT(@Body question: QuestionRequest): Call<GPTResponse>
}
