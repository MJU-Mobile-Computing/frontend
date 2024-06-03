package com.example.mc_project


import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportViewModel : ViewModel() {

    val monthYear: String
    val goalAchievementPercentage: Int
    val goalAchievedDaysText: String
    val averageIntake: Int
    val averageConsumption: Int
    val averageFastingTime: Int

    init {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        monthYear = sdf.format(calendar.time)

        // 여기에 실제 데이터를 기반으로 값을 계산하도록 설정
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val goalAchievedDays = calculateGoalAchievedDays(daysInMonth)
        goalAchievementPercentage = (goalAchievedDays * 100) / daysInMonth
        goalAchievedDaysText = "${daysInMonth}일 중 ${goalAchievedDays}일 지켰어요!"
        averageIntake = calculateAverageIntake()
        averageConsumption = calculateAverageConsumption()
        averageFastingTime = calculateAverageFastingTime()
    }

    private fun calculateGoalAchievedDays(daysInMonth: Int): Int {
        // 목표 달성 일수 계산 로직 (예시)
        return (0 until daysInMonth).count { Math.random() > 0.5 }
    }

    private fun calculateAverageIntake(): Int {
        // 평균 섭취 칼로리 계산 로직 (예시)
        return (1500..2500).random()
    }

    private fun calculateAverageConsumption(): Int {
        // 평균 소비 칼로리 계산 로직 (예시)
        return (1800..3000).random()
    }

    private fun calculateAverageFastingTime(): Int {
        // 평균 단식 시간 계산 로직 (예시)
        return (12..18).random()
    }
}
