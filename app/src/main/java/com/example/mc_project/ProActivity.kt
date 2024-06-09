package com.example.mc_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.databinding.ActivityProBinding
import com.google.android.material.tabs.TabLayoutMediator

class ProActivity : BaseActivity() {

    private lateinit var binding: ActivityProBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addBackButton()

        // ViewPager와 TabLayout 연결
        val viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(this)

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "닭가슴살"
                1 -> "뉴스 기사"
                else -> "기타"
            }
        }.attach()
    }
}
