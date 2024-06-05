@file:Suppress("DEPRECATION")

package com.example.mc_project

import android.os.Bundle
import com.example.mc_project.databinding.ActivityMainBinding
import android.content.Intent
import android.widget.ProgressBar
import android.widget.TextView
import com.example.mc_project.models.MainPageResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.appbar.MaterialToolbar // MaterialToolbar를 import
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // MaterialToolbar 가져오기
        val topAppBar: MaterialToolbar = findViewById(R.id.topAppBar)

        // 캘린더 버튼 클릭 이벤트 처리
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_calendar -> {
                    // 캘린더 액티비티로 이동
                    val intent = Intent(this@MainActivity, CalendarActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menu_mypage -> {
                    // 마이페이지 액티비티로 이동
                    val intent = Intent(this@MainActivity, MyPageActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // BottomNavigationView 가져오기
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // BottomNavigationView 아이템 선택 이벤트 처리
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_diet -> {
                    // 식단 등록 기능 실행
                    val intent = Intent(this@MainActivity, FoodRegistrationActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_timer -> {
                    // 간헐적 단식 타이머 실행
                    val intent = Intent(this@MainActivity, TimerActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_report -> {
                    // 먼슬리 보고서 실행
                    val intent = Intent(this@MainActivity, ReportActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_pro -> {
                    // PRO(닭가슴살 정보) 실행
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
                        updateUI(it.totalCalories, it.totalCarbohydrate, it.totalProteins, it.totalFat)
                    }
                }
            }

            override fun onFailure(call: Call<MainPageResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun updateUI(calories: Double, carbs: Double, proteins: Double, fat: Double) {
        val intakeCaloriesTextView: TextView = findViewById(R.id.tvIntakeValue)
        val remainingCaloriesTextView: TextView = findViewById(R.id.tvRemainingCalories)
        val carbsProgressBar: ProgressBar = findViewById(R.id.pbCarbs)
        val carbsProgressTextView: TextView = findViewById(R.id.tvCarbsProgress)
        val proteinProgressBar: ProgressBar = findViewById(R.id.pbProtein)
        val proteinProgressTextView: TextView = findViewById(R.id.tvProteinProgress)
        val fatProgressBar: ProgressBar = findViewById(R.id.pbFat)
        val fatProgressTextView: TextView = findViewById(R.id.tvFatProgress)
        val burnedValueTextView: TextView = findViewById(R.id.tvBurnedValue)
        val remainingCaloriesProgressBar: ProgressBar = findViewById(R.id.pbRemainingCalories)

        // Assume some burned calories value for demonstration
        val burnedCalories = 500.0
        burnedValueTextView.text = burnedCalories.toInt().toString()

        intakeCaloriesTextView.text = calories.toInt().toString()
        remainingCaloriesTextView.text = "${calories.toInt()} cal"

        carbsProgressBar.progress = carbs.toInt()
        carbsProgressTextView.text = "${carbs.toInt()}/327g"

        proteinProgressBar.progress = proteins.toInt()
        proteinProgressTextView.text = "${proteins.toInt()}/131g"

        fatProgressBar.progress = fat.toInt()
        fatProgressTextView.text = "${fat.toInt()}/87g"

        // Update the circular progress bar
        val progress = calculateProgress(calories, 2700) // Assuming 2700 is the goal
        remainingCaloriesProgressBar.progress = progress
    }

    private fun calculateProgress(current: Double, goal: Int): Int {
        return ((current / goal) * 100).toInt()
    }
}
