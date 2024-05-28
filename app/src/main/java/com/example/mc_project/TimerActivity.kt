package com.example.mc_project

import android.app.TimePickerDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.BaseActivity
import com.google.android.material.appbar.MaterialToolbar
import java.util.*

class TimerActivity : BaseActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var ssButton: Button
    private lateinit var resetButton: Button

    private var isRunning = false
    private var timeLeftInMillis: Long = 7200000 // 2 hours in milliseconds
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)


        timerTextView = findViewById(R.id.timer)
        ssButton = findViewById(R.id.ssbutton)
        resetButton = findViewById(R.id.resetButton)

        ssButton.setOnClickListener {
            if (isRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        resetButton.setOnClickListener {
            resetTimer()
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
            }
        }.start()

        isRunning = true
        ssButton.text = "타이머 일시정지"
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        isRunning = false
        ssButton.text = "타이머 시작하기"
    }

    private fun resetTimer() {
        timeLeftInMillis = 7200000 // 2 hours in milliseconds
        updateCountDownText()
        ssButton.text = "타이머 시작하기"
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        isRunning = false
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val selectedTimeInMillis = (selectedHour * 3600 + selectedMinute * 60) * 1000L
            timeLeftInMillis = selectedTimeInMillis
            resetTimer()
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun updateCountDownText() {
        val hours = (timeLeftInMillis / 1000) / 3600
        val minutes = ((timeLeftInMillis / 1000) % 3600) / 60
        val seconds = (timeLeftInMillis / 1000) % 60

        val timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        timerTextView.text = timeFormatted
    }
}
