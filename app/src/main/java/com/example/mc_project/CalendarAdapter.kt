package com.example.mc_project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mc_project.databinding.ItemDayBinding

class CalendarAdapter(private val dayInfoList: List<DayInfo>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDayBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayInfo = dayInfoList[position]
        holder.bind(dayInfo)
    }

    override fun getItemCount() = dayInfoList.size

    //랜덤하게 도달 여부 생성 + 요일에 맞춰서 빈칸 생성
    class ViewHolder(private val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dayInfo: DayInfo) {
            if (dayInfo.day == 0) {
                binding.tvDay.text = ""
                binding.tvDay.setBackgroundColor(ContextCompat.getColor(binding.root.context, android.R.color.white))
            } else {
                binding.tvDay.text = dayInfo.day.toString()
                if (dayInfo.metCalorieGoal) {
                    binding.tvDay.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.dark_green))
                } else {
                    binding.tvDay.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.dark_red))
                }
            }
        }
    }
}
