package com.example.mc_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

//뒤로가기 기능을 수행하도록 해주는 코틀린 코드
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun addBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
