package com.example.mc_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mc_project.databinding.ActivityFoodRegistrationBinding
import com.example.mc_project.models.ExerciseRegistrationRequest
import com.example.mc_project.models.ExerciseRegistrationResponse
import com.example.mc_project.models.FoodRegistrationRequest
import com.example.mc_project.models.FoodRegistrationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodRegistrationActivity : BaseActivity() {

    private lateinit var binding: ActivityFoodRegistrationBinding
    private var totalCalories = 0
    private val FOOD_SEARCH_REQUEST_CODE = 100
    private var currentMealType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기 버튼 추가
        addBackButton()

        binding.btnAddBreakfast.setOnClickListener { openFoodSearchActivity("아침") }
        binding.btnAddLunch.setOnClickListener { openFoodSearchActivity("점심") }
        binding.btnAddDinner.setOnClickListener { openFoodSearchActivity("저녁") }
        binding.btnAddSnack.setOnClickListener { openFoodSearchActivity("간식") }


        binding.btnRegisterExercise.setOnClickListener {
            val exerciseHour = binding.etExerciseHour.text.toString()
            if (exerciseHour.isNotEmpty()) {
                registerExercise(exerciseHour)
            } else {
                Toast.makeText(this, "운동 시간을 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FOOD_SEARCH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedFood = data?.getStringExtra("selectedFood")
            val calories = data?.getIntExtra("calories", 0) ?: 0
            val carbohydrates = data?.getDoubleExtra("carbohydrates", 0.0) ?: 0.0
            val proteins = data?.getDoubleExtra("proteins", 0.0) ?: 0.0
            val fat = data?.getDoubleExtra("fat", 0.0) ?: 0.0
            Log.d("FoodRegistration", "Selected Food: $selectedFood, Calories: $calories")
            selectedFood?.let {
                displaySelectedFood(it, calories)
                addMealCalories(calories)

                // 음식 등록 API 호출
                registerFood(
                    calories.toString(),
                    carbohydrates.toString(),
                    proteins.toString(),
                    fat.toString(),
                    currentMealType ?: ""
                )
            }
        }
    }

    private fun openFoodSearchActivity(mealType: String) {
        currentMealType = mealType
        val intent = Intent(this, FoodSearchActivity::class.java)
        startActivityForResult(intent, FOOD_SEARCH_REQUEST_CODE)
    }

    private fun displaySelectedFood(selectedFood: String, calories: Int) {
        when (currentMealType) {
            "아침" -> {
                binding.tvSelectedBreakfastFood.text = selectedFood
                binding.tvSelectedBreakfastCalories.text = "칼로리: $calories kcal"
            }

            "점심" -> {
                binding.tvSelectedLunchFood.text = selectedFood
                binding.tvSelectedLunchCalories.text = "칼로리: $calories kcal"
            }

            "저녁" -> {
                binding.tvSelectedDinnerFood.text = selectedFood
                binding.tvSelectedDinnerCalories.text = "칼로리: $calories kcal"
            }

            "간식" -> {
                binding.tvSelectedSnackFood.text = selectedFood
                binding.tvSelectedSnackCalories.text = "칼로리: $calories kcal"
            }
        }
    }

    private fun addMealCalories(calories: Int) {
        totalCalories += calories
        // 총 칼로리를 표시하는 TextView를 업데이트
        binding.tvTotalCalories.text = "총 칼로리: $totalCalories kcal"
    }

    private fun registerFood(
        calories: String,
        carbs: String,
        proteins: String,
        fat: String,
        mealType: String
    ) {
        val request = FoodRegistrationRequest(calories, carbs, proteins, fat, mealType)
        RetrofitInstance.api.registerFood(request)
            .enqueue(object : Callback<FoodRegistrationResponse> {
                override fun onResponse(
                    call: Call<FoodRegistrationResponse>,
                    response: Response<FoodRegistrationResponse>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.data?.message ?: "Unknown response"
                        Log.d("FoodRegistration", message)
                        // 성공적인 응답 처리 (예: UI 업데이트 또는 메시지 표시)
                    } else {
                        Log.e(
                            "FoodRegistration",
                            "Failed to register food: ${response.errorBody()?.string()}"
                        )
                    }
                }

                override fun onFailure(call: Call<FoodRegistrationResponse>, t: Throwable) {
                    Log.e("FoodRegistration", "API call failed", t)
                }
            })
    }

    private fun registerExercise(exerciseHour: String) {
        val request = ExerciseRegistrationRequest(exerciseHour)
        RetrofitInstance.api.registerExercise(request)
            .enqueue(object : Callback<ExerciseRegistrationResponse> {
                override fun onResponse(
                    call: Call<ExerciseRegistrationResponse>,
                    response: Response<ExerciseRegistrationResponse>
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.data?.message ?: "Unknown response"
                        Log.d("ExerciseRegistration", message)

                        // 운동 등록이 성공하면 운동 시간을 메인 엑티비티로 전달하여 업데이트
                        val intent = Intent()
                        intent.putExtra("exerciseHour", exerciseHour)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else {
                        Log.e(
                            "ExerciseRegistration",
                            "Failed to register exercise: ${response.errorBody()?.string()}"
                        )
                    }
                }

                override fun onFailure(call: Call<ExerciseRegistrationResponse>, t: Throwable) {
                    Log.e("ExerciseRegistration", "API call failed", t)
                }
            })
    }
}
