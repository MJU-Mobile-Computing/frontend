package com.example.mc_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.mc_project.databinding.ActivityEditgoalsBinding

class EditGoalsActivity : BaseActivity() {

    private lateinit var binding: ActivityEditgoalsBinding
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditgoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addBackButton()

        ArrayAdapter.createFromResource(
            this,
            R.array.mygoal_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGoal.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.activity_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerActivityLevel.adapter = adapter
        }

        //setInitialValues()

        binding.buttonEdit.setOnClickListener {
            toggleEditMode()
        }
    }

    private fun setInitialValues() {
        binding.editTextCurrentWeight.setText("70")
        binding.editTextGoalWeight.setText("65")
        binding.editTextGoalSteps.setText("10000")
    }

    private fun toggleEditMode() {
        isEditing = !isEditing

        binding.editTextCurrentWeight.isEnabled = isEditing
        binding.editTextGoalWeight.isEnabled = isEditing
        binding.editTextGoalSteps.isEnabled = isEditing
        binding.spinnerGoal.isEnabled = isEditing
        binding.spinnerActivityLevel.isEnabled = isEditing

        if (isEditing) {
            binding.buttonEdit.text = "저장"
        } else {
            binding.buttonEdit.text = "수정"
            saveGoals()
        }
    }

    override fun onBackPressed() {
        saveGoals() // 뒤로가기 버튼을 누를 때도 목표를 저장
        super.onBackPressed()
    }

    private fun saveGoals() {
        val goal = binding.spinnerGoal.selectedItem.toString()
        val currentWeight = binding.editTextCurrentWeight.text.toString()
        val goalWeight = binding.editTextGoalWeight.text.toString()
        val activityLevel = binding.spinnerActivityLevel.selectedItem.toString()
        val goalSteps = binding.editTextGoalSteps.text.toString()

        if (currentWeight.toDoubleOrNull() == null || currentWeight.toDouble() <= 0) {
            Toast.makeText(this, "유효한 현재 몸무게를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (goalWeight.toDoubleOrNull() == null || goalWeight.toDouble() <= 0) {
            Toast.makeText(this, "유효한 목표 몸무게를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if (goalSteps.toDoubleOrNull() == null || goalSteps.toDouble() <= 0) {
            Toast.makeText(this, "유효한 목표 걸음 수를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val resultIntent = Intent()
        val bundle = Bundle().apply {
            putString("goal", goal)
            putString("currentWeight", currentWeight)
            putString("goalWeight", goalWeight)
            putString("activityLevel", activityLevel)
            putString("goalSteps", goalSteps)
        }
        resultIntent.putExtras(bundle)
        setResult(Activity.RESULT_OK, resultIntent)
        Toast.makeText(this, "목표가 저장되었습니다", Toast.LENGTH_SHORT).show()
    }
}
