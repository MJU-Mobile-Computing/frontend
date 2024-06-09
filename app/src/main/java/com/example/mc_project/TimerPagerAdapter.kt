package com.example.mc_project

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TimerPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TimerFragment()
            1 -> IntermittentFastingFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
