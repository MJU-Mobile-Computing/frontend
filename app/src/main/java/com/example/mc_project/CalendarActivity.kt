package com.example.mc_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.databinding.ActivityCalendarBinding
import java.io.IOException
import java.util.Calendar

class CalendarActivity : BaseActivity() {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var fName: String // 파일 이름을 멤버 변수로 선언합니다.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기 버튼 추가
        addBackButton()

        val cal = Calendar.getInstance()
        val cYear = cal.get(Calendar.YEAR)
        val cMonth = cal.get(Calendar.MONTH)
        val cDay = cal.get(Calendar.DAY_OF_MONTH)

        // CalendarView 초기화
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            fName = "$year-${month + 1}-$dayOfMonth.txt"
            loadDiary()
        }

        // 새로 저장 / 수정하기 버튼 클릭 리스너
        binding.btnSmt.setOnClickListener {
            saveDiary()
        }

        // 초기 로드
        fName = "$cYear-${cMonth + 1}-$cDay.txt"
        loadDiary()
    }

    // 다이어리 내용 로드
    private fun loadDiary() {
        val diaryContent = readDiary(fName)
        if (diaryContent.isNullOrEmpty()) {
            binding.editText.setHint("일기 없음")
            binding.btnSmt.text = "새로 저장"
            binding.editText.setText("")
        } else {
            binding.editText.setText(diaryContent)
            binding.btnSmt.text = "수정하기"
        }
    }

    // 다이어리 내용 저장
    private fun saveDiary() {
        val diaryContent = binding.editText.text.toString()
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
}
