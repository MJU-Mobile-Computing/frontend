package com.example.mc_project

import com.example.mc_project.models.ApiResponse
import com.example.mc_project.models.ExerciseRegistrationRequest
import com.example.mc_project.models.ExerciseRegistrationResponse
import com.example.mc_project.models.FoodRegistrationRequest
import com.example.mc_project.models.FoodRegistrationResponse
import com.example.mc_project.models.QuestionRequest
import com.example.mc_project.models.GPTResponse
import com.example.mc_project.models.GoalCaloriesRequest
import com.example.mc_project.models.MainPageResponse
import com.example.mc_project.models.MessageResponse
import com.example.mc_project.models.MyPageData
import com.example.mc_project.models.MyPageGoalData
import com.example.mc_project.models.MyPageResponse
import com.example.mc_project.models.SearchFoodResponse
import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface FoodApiService {
    @GET("foods")
    fun searchFoods(@Query("foodName") foodName: String): Call<SearchFoodResponse>

    @POST("api/v1/chat-gpt")
    fun askGPT(@Body question: QuestionRequest): Call<GPTResponse>

    @PATCH("mypage")
    fun updateMyPageGoalData(@Body data: MyPageGoalData): Call<MyPageResponse>

    @GET("mainpage")
    fun getMainPageData(): Call<MainPageResponse>

    @GET("mypage")
    fun getMyPageData(): Call<MyPageResponse>

    @PATCH("mypage")
    fun updateMyPageData(@Body data: MyPageData): Call<MyPageResponse>

    @POST("foods/register")
    fun registerFood(@Body request: FoodRegistrationRequest): Call<FoodRegistrationResponse>

    @POST("exercise/register")
    fun registerExercise(@Body request: ExerciseRegistrationRequest): Call<ExerciseRegistrationResponse>

    @POST("bookmark/register")
    fun bookmarkFood(@Body jsonObject: JsonObject): Call<ApiResponse>

    @DELETE("bookmark/delete")
    fun deleteBookmark(@Query("foodId") foodId: Int): Call<ApiResponse>

    @GET("mainpage/date")
    fun getMainPageDataByDate(@Query("date") date: String): Call<MainPageResponse>

    @POST("mainpage/goalCalories")
    fun setGoalCalories(@Body request: GoalCaloriesRequest): Call<MessageResponse>
}
