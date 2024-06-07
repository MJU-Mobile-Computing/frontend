package com.example.mc_project

import android.os.Bundle
import com.example.mc_project.databinding.ActivityMainBinding
import android.content.Intent
import android.widget.ProgressBar
import android.widget.TextView
import com.example.mc_project.models.MainPageResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.appbar.MaterialToolbar
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
    private val dailyCalorieGoal = 2700.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
                    startActivityForResult(intent, FOOD_REGISTRATION_REQUEST_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FOOD_REGISTRATION_REQUEST_CODE && resultCode == RESULT_OK) {
            val additionalCalories = data?.getIntExtra("totalCalories", 0) ?: 0
            intakeCalories += additionalCalories
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
                        burnedCalories = it.totalBurnedCalories.toInt() // 소비량 업데이트
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
        val remainingCalories = dailyCalorieGoal - intakeCalories + burnedCalories // 소비량을 고려하여 남은 칼로리 계산
        remainingCaloriesTextView.text = "${remainingCalories.toInt()} cal"

        val progress = calculateProgress(remainingCalories, dailyCalorieGoal)
        remainingCaloriesProgressBar.progress = progress
    }

    private fun calculateProgress(current: Double, goal: Double): Int {
        return ((current / goal) * 100).toInt()
    }
}
