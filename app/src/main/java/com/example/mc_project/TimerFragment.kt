package com.example.mc_project

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class TimerFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        timerTextView = view.findViewById(R.id.timer)
        ssButton = view.findViewById(R.id.ssbutton)
        resetButton = view.findViewById(R.id.resetButton)
        timerIcon = view.findViewById(R.id.timerIcon)
        startTimeTextView = view.findViewById(R.id.startTimeTextView)
        endTimeTextView = view.findViewById(R.id.endTimeTextView)

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

        return view
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
