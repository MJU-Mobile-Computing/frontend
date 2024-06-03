package com.example.mc_project

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mc_project.databinding.ActivityMainBinding
import com.example.mc_project.databinding.ActivityReportBinding
import java.util.Calendar

class ReportActivity : BaseActivity() {

    private lateinit var binding: ActivityReportBinding
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var viewModel: ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewModelProvider를 사용하여 ViewModel 초기화
        viewModel = ViewModelProvider(this).get(ReportViewModel::class.java)

        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뷰모델 데이터를 바인딩
        binding.tvMonthYear.text = viewModel.monthYear
        binding.goalAchievementBar.progress = viewModel.goalAchievementPercentage
        binding.tvGoalAchievedDays.text = viewModel.goalAchievedDaysText
        binding.tvAverageIntake.text = "평균 칼로리 섭취: ${viewModel.averageIntake} kcal"
        binding.tvAverageConsumption.text = "평균 칼로리 소비: ${viewModel.averageConsumption} kcal"
        binding.tvAverageFastingTime.text = "평균 단식 시간: ${viewModel.averageFastingTime} 시간"

        binding.calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)

        val dayInfoList = ArrayList<DayInfo>()
        populateCalendar(dayInfoList)

        calendarAdapter = CalendarAdapter(dayInfoList)
        binding.calendarRecyclerView.adapter = calendarAdapter

//       각 날짜의 칼로리 섭취 그래프 변화.. 단백질 기타영양소 등등 더 자세히 확인
        binding.btnMoreDetails.setOnClickListener {
            Toast.makeText(this, "More details button clicked", Toast.LENGTH_SHORT).show()
            // 여기에 더 자세히 확인하기 버튼 클릭 시 실행할 로직을 추가하세요.
        }
    }

    //랜덤 생성 예제
    private fun populateCalendar(dayInfoList: ArrayList<DayInfo>) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1  // Calendar.DAY_OF_WEEK는 일요일을 1로 시작

        // 빈 날짜를 채워 넣기
        for (i in 0 until firstDayOfWeek) {
            dayInfoList.add(DayInfo(0, false))  // 0을 사용하여 빈 날짜로 표시
        }

        // 실제 날짜를 채워 넣기
        for (day in 1..daysInMonth) {
            dayInfoList.add(DayInfo(day, Math.random() > 0.5))
        }
    }
}
