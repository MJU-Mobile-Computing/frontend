package com.example.mc_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.mc_project.databinding.ActivityEditprofileBinding

class EditProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityEditprofileBinding
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addBackButton()

        ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGender.adapter = adapter
        }

        binding.buttonEdit.setOnClickListener {
            toggleEditMode()
        }
    }

    private fun toggleEditMode() {
        isEditing = !isEditing

        binding.editTextLastName.isEnabled = isEditing
        binding.editTextFirstName.isEnabled = isEditing
        binding.spinnerGender.isEnabled = isEditing
        binding.datePickerDOB.isEnabled = isEditing
        binding.editTextHeight.isEnabled = isEditing

        binding.editTextLastName.visibility = if (isEditing) View.VISIBLE else View.GONE
        binding.editTextFirstName.visibility = if (isEditing) View.VISIBLE else View.GONE
        binding.spinnerGender.visibility = if (isEditing) View.VISIBLE else View.GONE
        binding.datePickerDOB.visibility = if (isEditing) View.VISIBLE else View.GONE
        binding.editTextHeight.visibility = if (isEditing) View.VISIBLE else View.GONE

        if (isEditing) {
            binding.buttonEdit.text = "저장"
        } else {
            binding.buttonEdit.text = "수정"
            saveProfile()
        }
    }

//    override fun onBackPressed() {
//        saveProfile() // 뒤로가기 버튼을 누를 때도 프로필을 저장
//        super.onBackPressed()
//    }

    private fun saveProfile() {
        val lastName = binding.editTextLastName.text.toString()
        val firstName = binding.editTextFirstName.text.toString()
        val gender = binding.spinnerGender.selectedItem.toString()
        val dob = "${binding.datePickerDOB.year}-${binding.datePickerDOB.month + 1}-${binding.datePickerDOB.dayOfMonth}"
        val height = binding.editTextHeight.text.toString()
        val age = (2024 - binding.datePickerDOB.year).toString()

        val resultIntent = Intent()
        val bundle = Bundle().apply {
            putString("lastName", lastName)
            putString("firstName", firstName)
            putString("gender", gender)
            putString("dob", dob)
            putString("age", age)
            putString("height", height)
        }
        resultIntent.putExtras(bundle)
        setResult(RESULT_OK, resultIntent)
        Toast.makeText(this, "프로필이 저장되었습니다", Toast.LENGTH_SHORT).show()
    }
}
