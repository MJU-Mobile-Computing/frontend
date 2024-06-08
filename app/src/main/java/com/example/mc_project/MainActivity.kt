package com.example.mc_project

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import com.example.mc_project.databinding.ActivityMainBinding
import com.example.mc_project.models.MainPageResponse
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val FOOD_REGISTRATION_REQUEST_CODE = 101
    private var intakeCalories = 0.0
    private var burnedCalories = 0 // 소비량 변수 추가
    private var dailyCalorieGoal = 2700.0 // 기본값, SharedPreferences에서 업데이트됨

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences에서 목표 칼로리 가져오기
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        dailyCalorieGoal = sharedPreferences.getInt("goalCalorie", 2700).toDouble()

        // MaterialToolbar 가져오기
        val topAppBar: MaterialToolbar = findViewById(R.id.topAppBar)
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_calendar -> {
                    val intent = Intent(this@MainActivity, CalendarActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_mypage -> {
                    val intent = Intent(this@MainActivity, MyPageActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_refresh -> {
                    fetchDataFromApi()
                    true
                }
                else -> false
            }
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_diet -> {
                    val intent = Intent(this@MainActivity, FoodRegistrationActivity::class.java)
                    startActivityForResult(intent, FOOD_REGISTRATION_REQUEST_CODE)
                    true
                }
                R.id.nav_timer -> {
                    val intent = Intent(this@MainActivity, TimerActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_report -> {
                    val intent = Intent(this@MainActivity, ReportActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_pro -> {
                    val intent = Intent(this@MainActivity, ProActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Fetch data from API using Retrofit
        fetchDataFromApi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FOOD_REGISTRATION_REQUEST_CODE && resultCode == RESULT_OK) {
            val additionalCalories = data?.getIntExtra("totalCalories", 0) ?: 0
            intakeCalories += additionalCalories

            val exerciseHour = data?.getStringExtra("exerciseHour")
            if (exerciseHour != null) {
                val burnedCaloriesFromExercise = exerciseHour.toInt() * 200
                burnedCalories += burnedCaloriesFromExercise
            }

            updateIntakeAndRemainingCalories()
        }
    }

    private fun fetchDataFromApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.200.181.134:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(FoodApiService::class.java)

        apiService.getMainPageData().enqueue(object : Callback<MainPageResponse> {
            override fun onResponse(call: Call<MainPageResponse>, response: Response<MainPageResponse>) {
                if (response.isSuccessful) {
                    val mainPageData = response.body()?.data
                    mainPageData?.let {
                        intakeCalories = it.totalCalories
                        burnedCalories = it.totalBurnedCalories.toInt()
                        updateUI(it.totalCalories, it.totalCarbohydrate, it.totalProteins, it.totalFat, it.totalBurnedCalories)
                        updateIntakeAndRemainingCalories()
                    }
                }
            }

            override fun onFailure(call: Call<MainPageResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun updateUI(calories: Double, carbs: Double, proteins: Double, fat: Double, burned: Long) {
        val carbsProgressBar: ProgressBar = findViewById(R.id.pbCarbs)
        val carbsProgressTextView: TextView = findViewById(R.id.tvCarbsProgress)
        val proteinProgressBar: ProgressBar = findViewById(R.id.pbProtein)
        val proteinProgressTextView: TextView = findViewById(R.id.tvProteinProgress)
        val fatProgressBar: ProgressBar = findViewById(R.id.pbFat)
        val fatProgressTextView: TextView = findViewById(R.id.tvFatProgress)
        val burnedValueTextView: TextView = findViewById(R.id.tvBurnedValue)

        burnedValueTextView.text = burned.toString()

        carbsProgressBar.progress = carbs.toInt()
        carbsProgressTextView.text = "${carbs.toInt()}/327g"

        proteinProgressBar.progress = proteins.toInt()
        proteinProgressTextView.text = "${proteins.toInt()}/131g"

        fatProgressBar.progress = fat.toInt()
        fatProgressTextView.text = "${fat.toInt()}/87g"
    }

    private fun updateIntakeAndRemainingCalories() {
        val intakeCaloriesTextView: TextView = findViewById(R.id.tvIntakeValue)
        val remainingCaloriesTextView: TextView = findViewById(R.id.tvRemainingCalories)
        val remainingCaloriesProgressBar: ProgressBar = findViewById(R.id.pbRemainingCalories)

        intakeCaloriesTextView.text = intakeCalories.toInt().toString()
        val remainingCalories = dailyCalorieGoal - intakeCalories + burnedCalories
        remainingCaloriesTextView.text = "${remainingCalories.toInt()} cal"

        val progress = calculateProgress(remainingCalories, dailyCalorieGoal)
        remainingCaloriesProgressBar.progress = progress
    }

    private fun calculateProgress(current: Double, goal: Double): Int {
        return ((current / goal) * 100).toInt()
    }
}
