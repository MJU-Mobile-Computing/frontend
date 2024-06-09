package com.example.mc_project

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mc_project.databinding.ActivityCalendarBinding
import com.example.mc_project.models.GoalCaloriesRequest
import com.example.mc_project.models.MainPageResponse
import com.example.mc_project.models.MessageResponse
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : BaseActivity() {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var fName: String // 파일 이름을 멤버 변수로 선언합니다.
    private val diaryData = mutableMapOf<String, String>() // 모든 다이어리 데이터를 저장합니다.

    companion object {
        const val GOAL_CALORIE = 2000 // 예시 목표 칼로리
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addBackButton()

        val cal = Calendar.getInstance()
        val cYear = cal.get(Calendar.YEAR)
        val cMonth = cal.get(Calendar.MONTH)
        val cDay = cal.get(Calendar.DAY_OF_MONTH)

        // 초기 로드
        fName = "$cYear-${cMonth + 1}-$cDay.txt"
        loadDiary()

        // 모든 다이어리 데이터를 로드
        loadAllDiaries()

        // CalendarView 초기화
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            fName = "${date.year}-${date.month + 1}-${date.day}.txt"
            loadDiary()
        }

        // 새로 저장 / 수정하기 버튼 클릭 리스너
        binding.btnSmt.setOnClickListener {
            saveDiary()
            loadAllDiaries()
        }

        // 권장 소비량 텍스트 뷰 업데이트
        binding.recommendedCaloriesTextView.text = "목표 권장 소비량: ${SharedData.remainingCalories.toInt()} kcal"

        // 서버에서 목표 칼로리 값을 가져옴
        fetchGoalCalories()
    }

    // 모든 다이어리 데이터를 로드
    private fun loadAllDiaries() {
        diaryData.clear()
        val files = fileList()
        Log.d("CalendarActivity", "Files: ${files.joinToString(", ")}")
        files.filter { it.endsWith(".txt") }.forEach { fileName ->
            try {
                val content = readDiary(fileName)
                Log.d("CalendarActivity", "Read file $fileName with content: $content")
                if (content != null) {
                    diaryData[fileName] = content
                }
            } catch (e: IOException) {
                Log.e("CalendarActivity", "Error reading file $fileName", e)
            }
        }
        highlightDates()
    }

    // 날짜를 강조 표시하는 메서드
    private fun highlightDates() {
        binding.calendarView.removeDecorators()
        diaryData.forEach { (fileName, content) ->
            val dateStr = fileName.replace(".txt", "")
            val dateParts = dateStr.split("-")
            if (dateParts.size == 3) {
                val year = dateParts[0].toIntOrNull()
                val month = dateParts[1].toIntOrNull()
                val day = dateParts[2].toIntOrNull() // 일자도 파싱합니다.
                Log.d("CalendarActivity", "Parsed date for highlight: $year-$month-$day")

                if (year != null && month != null && day != null) {
                    val date = CalendarDay.from(year, month - 1, day)
                    val calories = content.split("\n").getOrNull(0)?.toIntOrNull()
                    if (calories != null) {
                        val color = if (calories > GOAL_CALORIE) Color.RED else Color.GREEN
                        binding.calendarView.addDecorator(HighlightDecorator(date, color))
                    }
                }
            }
        }
        Log.d("CalendarActivity", "Highlighted dates: ${diaryData.keys.joinToString(", ")}")
    }

    // 다이어리 내용 로드
    private fun loadDiary() {
        val diaryContent = readDiary(fName)
        if (diaryContent.isNullOrEmpty()) {
            binding.editTextCalories.setHint("총 칼로리 없음")
            binding.editTextWeight.setHint("공복 시 체중 없음")
            binding.btnSmt.text = "새로 저장"
            binding.editTextCalories.setText("")
            binding.editTextWeight.setText("")
        } else {
            val contents = diaryContent.split("\n")
            binding.editTextCalories.setText(contents.getOrNull(0) ?: "")
            binding.editTextWeight.setText(contents.getOrNull(1) ?: "")
            binding.btnSmt.text = "수정하기"
        }
        Log.d("CalendarActivity", "Loaded diary file $fName with content: $diaryContent")
    }

    // 다이어리 내용 저장
    private fun saveDiary() {
        val caloriesContent = binding.editTextCalories.text.toString()
        val weightContent = binding.editTextWeight.text.toString()
        val diaryContent = "$caloriesContent\n$weightContent"
        openFileOutput(fName, MODE_PRIVATE).use { outputStream ->
            outputStream.write(diaryContent.toByteArray())
        }
    }

    // 다이어리 내용 읽기
    private fun readDiary(fName: String): String? {
        return try {
            openFileInput(fName).bufferedReader().use { reader ->
                reader.readText()
            }
        } catch (e: IOException) {
            null
        }
    }

    private fun fetchGoalCalories() {
        val currentDate = getCurrentDate()
        RetrofitInstance.api.getMainPageDataByDate(currentDate).enqueue(object : Callback<MainPageResponse> {
            override fun onResponse(call: Call<MainPageResponse>, response: Response<MainPageResponse>) {
                if (response.isSuccessful) {
                    val goalCalories = response.body()?.data?.goalCalories ?: 0f
                    binding.editTextCalories.setText(goalCalories.toString())
                } else {
                    Log.e("CalendarActivity", "Failed to fetch goal calories")
                }
            }

            override fun onFailure(call: Call<MainPageResponse>, t: Throwable) {
                Log.e("CalendarActivity", "API call failed", t)
            }
        })
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    // Decorator 클래스
    class HighlightDecorator(private val date: CalendarDay, private val color: Int) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == date
        }

        override fun decorate(view: DayViewFacade) {
            view.setBackgroundDrawable(ColorDrawable(color))
        }
    }
}
