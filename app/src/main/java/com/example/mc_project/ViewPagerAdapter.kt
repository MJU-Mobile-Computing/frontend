package com.example.mc_project

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2 // Tab의 개수
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChickenFragment()
            1 -> NewsFragment()
            else -> Fragment()
        }
    }
}
