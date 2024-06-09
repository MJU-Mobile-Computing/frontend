package com.example.mc_project

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.databinding.ActivityMainBinding
import com.example.mc_project.models.MainPageResponse
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val FOOD_REGISTRATION_REQUEST_CODE = 101
    private var intakeCalories = 0.0
    private var burnedCalories = 0
    private var dailyCalorieGoal = 2700.0
    private lateinit var dateTextView: TextView
    private var currentDate: String

    init {
        // 시스템 날짜로 currentDate 초기화
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        currentDate = sdf.format(Date())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set current date on TextView
        dateTextView = findViewById(R.id.dateTextView)
        dateTextView.text = currentDate

        // 상단 좌측 텍스트 뷰 클릭 시 DatePickerDialog 표시
        dateTextView.setOnClickListener {
            showDatePickerDialog()
        }

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
                    fetchMainPageDataByDate(currentDate)  // 새로 고침 시에도 동일한 날짜 데이터를 요청
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
        fetchMainPageDataByDate(currentDate)
    }

    override fun onResume() {
        super.onResume()
        fetchMainPageDataByDate(currentDate)
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

    private fun fetchMainPageDataByDate(date: String) {
        RetrofitInstance.api.getMainPageDataByDate(date).enqueue(object : Callback<MainPageResponse> {
            override fun onResponse(call: Call<MainPageResponse>, response: Response<MainPageResponse>) {
                if (response.isSuccessful) {
                    val mainPageData = response.body()?.data
                    mainPageData?.let {
                        intakeCalories = it.totalCalories
                        burnedCalories = it.totalBurnedCalories.toInt()
                        dailyCalorieGoal = it.goalCalories // Update the goal calories
                        updateUI(it.totalCalories, it.totalCarbohydrate, it.totalProteins, it.totalFat, it.totalBurnedCalories)
                        updateIntakeAndRemainingCalories()
                    }
                } else {
                    Log.e("MainActivity", "Response failed")
                }
            }

            override fun onFailure(call: Call<MainPageResponse>, t: Throwable) {
                Log.e("MainActivity", "Request failed", t)
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

        // 잔여 칼로리 계산: 목표 칼로리 - 섭취량 + (운동 칼로리 * 400)
        val remainingCalories = dailyCalorieGoal - intakeCalories + (burnedCalories * 200)
        remainingCaloriesTextView.text = "${remainingCalories.toInt()} cal"

        val progress = calculateProgress(remainingCalories, dailyCalorieGoal)
        remainingCaloriesProgressBar.progress = progress
    }

    private fun calculateProgress(current: Double, goal: Double): Int {
        return ((current / goal) * 100).toInt()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)
                dateTextView.text = selectedDate
                fetchMainPageDataByDate(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}