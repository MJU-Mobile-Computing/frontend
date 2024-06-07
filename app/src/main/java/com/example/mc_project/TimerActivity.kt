package com.example.mc_project

import android.app.TimePickerDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class TimerActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var ssButton: Button
    private lateinit var resetButton: Button
    private lateinit var timerIcon: ImageView
    private lateinit var startTimeTextView: TextView
    private lateinit var endTimeTextView: TextView

    private var isRunning = false
    private var timeLeftInMillis: Long = 7200000 // 2 hours in milliseconds
    private lateinit var countDownTimer: CountDownTimer
    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timerTextView = findViewById(R.id.timer)
        ssButton = findViewById(R.id.ssbutton)
        resetButton = findViewById(R.id.resetButton)
        timerIcon = findViewById(R.id.timerIcon)
        startTimeTextView = findViewById(R.id.startTimeTextView)
        endTimeTextView = findViewById(R.id.endTimeTextView)

        ssButton.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
                startTime = System.currentTimeMillis()
                updateStartTime()
            }
        }

        resetButton.setOnClickListener {
            resetTimer()
            endTime = System.currentTimeMillis()
            updateEndTime()
        }

        updateCountDownText()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                isRunning = false
                ssButton.text = "타이머 시작하기"
                timerIcon.setImageResource(R.drawable.ic_egg2) // 타이머 종료 후 이미지 변경
            }
        }.start()

        isRunning = true
        ssButton.text = "타이머 일시정지"
        timerIcon.setImageResource(R.drawable.ic_egg1) // 타이머 시작 시 이미지 변경
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        isRunning = false
        ssButton.text = "타이머 시작하기"
        timerIcon.setImageResource(R.drawable.ic_egg2) // 타이머 일시정지 시 이미지 변경
    }

    private fun resetTimer() {
        timeLeftInMillis = 7200000 // 2 hours in milliseconds
        updateCountDownText()
        ssButton.text = "타이머 시작하기"
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        isRunning = false
        timerIcon.setImageResource(R.drawable.ic_egg2) // 리셋 시 이미지 변경
    }

    private fun updateStartTime() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = startTime
        val startTimeFormatted = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(calendar.time)
        startTimeTextView.text = "시작 시간: $startTimeFormatted"
        startTimeTextView.visibility = TextView.VISIBLE
    }

    private fun updateEndTime() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = endTime
        val endTimeFormatted = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(calendar.time)
        endTimeTextView.text = "종료 시간: $endTimeFormatted"
        endTimeTextView.visibility = TextView.VISIBLE
    }

    private fun updateCountDownText() {
        val hours = (timeLeftInMillis / 1000) / 3600
        val minutes = ((timeLeftInMillis / 1000) % 3600) / 60
        val seconds = (timeLeftInMillis / 1000) % 60

        val timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        timerTextView.text = timeFormatted
    }
}
