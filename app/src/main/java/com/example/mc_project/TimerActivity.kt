package com.example.mc_project

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import com.example.mc_project.databinding.ActivityTimerBinding

class TimerActivity : BaseActivity() {
    private lateinit var binding: ActivityTimerBinding
    private var time = 0L
    private var isRunning = false
    private var doubleClickDelay: Long = 300
    private var lastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기 버튼 추가
        addBackButton()

        // 버튼 처음 상태 (start)
        binding.ssbutton.text = getString(R.string.start)

        // Start, Stop 버튼 == ssbutton
        binding.ssbutton.setOnClickListener {
            //예외처리 버튼을 재빠르게 더블 클릭한 경우
            val currentTime = SystemClock.elapsedRealtime()
            if (currentTime - lastClickTime < doubleClickDelay) {
                showToast("start/stop button already double clicked!")
                return@setOnClickListener
            }
            lastClickTime = currentTime
            if (isRunning) {
                // 크로노미터를 stop 후 시간 업데이트
                binding.timer.stop()
                time = (SystemClock.elapsedRealtime() - binding.timer.base)

                // 버튼 "Start"로 바꾸기
                binding.ssbutton.text = getString(R.string.start)
                isRunning = false
            } else {
                // 크로노미터 start 후 초기 시간
                binding.timer.base = SystemClock.elapsedRealtime() - time
                binding.timer.start()

                // 버튼 "Stop"으로 바꾸기
                binding.ssbutton.text = getString(R.string.stop)
                isRunning = true
            }
        }

        binding.reset.setOnClickListener {
            time = 0L
            binding.timer.base = SystemClock.elapsedRealtime()
            binding.timer.stop()

            binding.ssbutton.text = getString(R.string.start)
            isRunning = false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}
