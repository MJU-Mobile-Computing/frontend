package com.example.mc_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mc_project.databinding.ActivityTimerBinding
import com.google.android.material.tabs.TabLayoutMediator

class TimerActivity : BaseActivity() {

    private lateinit var binding: ActivityTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addBackButton()

        // Set up ViewPager with the sections adapter.
        val viewPager = binding.viewPager
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        viewPager.adapter = sectionsPagerAdapter

        // Set up the TabLayout with ViewPager
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "타이머"
                1 -> "간헐적 단식"
                else -> null
            }
        }.attach()
    }

    inner class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TimerFragment()
                1 -> IntermittentFastingFragment()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }
}
