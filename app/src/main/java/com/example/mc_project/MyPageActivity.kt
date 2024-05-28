package com.example.mc_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MyPageActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        // 뒤로가기 버튼 추가
        addBackButton()
    }
}

