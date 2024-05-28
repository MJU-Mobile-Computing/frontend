@file:Suppress("DEPRECATION")

package com.example.mc_project

import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.appbar.MaterialToolbar // MaterialToolbar를 import

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    startActivity(intent)
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
    }
}
