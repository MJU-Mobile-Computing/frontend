package com.example.mc_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(private val foodList: List<Food>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFoodName: TextView = view.findViewById(R.id.tvFoodName)
        val tvFoodCalories: TextView = view.findViewById(R.id.tvFoodCalories)
        val tvFoodCarbohydrates: TextView = view.findViewById(R.id.tvFoodCarbohydrates)
        val tvFoodProtein: TextView = view.findViewById(R.id.tvFoodProtein)
        val tvFoodFat: TextView = view.findViewById(R.id.tvFoodFat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]
        holder.tvFoodName.text = food.foodName
        holder.tvFoodCalories.text = "${food.calories} 칼로리"
        holder.tvFoodCarbohydrates.text = "탄수화물: ${food.carbohydrates}g"
        holder.tvFoodProtein.text = "단백질: ${food.protein}g"
        holder.tvFoodFat.text = "지방: ${food.fat}g"
    }

    override fun getItemCount(): Int {
        return foodList.size
    }
}
