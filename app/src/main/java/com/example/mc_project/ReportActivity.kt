package com.example.mc_project

import android.os.Bundle

class ReportActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        // 뒤로가기 버튼 추가
        addBackButton()
    }
}