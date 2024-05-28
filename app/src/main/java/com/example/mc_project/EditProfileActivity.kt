package com.example.mc_project


import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.databinding.ActivityEditprofileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditprofileBinding
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 성별 스피너 설정
        ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGender.adapter = adapter
        }

        // 초기 입력값 설정
        setInitialValues()

        // 수정 버튼 클릭 시
        binding.buttonEdit.setOnClickListener {
            toggleEditMode()
        }
    }

    private fun setInitialValues() {
        binding.textViewLastName.text = "홍"
        binding.editTextLastName.setText("홍")

        binding.textViewFirstName.text = "길동"
        binding.editTextFirstName.setText("길동")

        binding.spinnerGender.setSelection(0) // 디폴트는 남성으로 설정

        binding.textViewDOB.text = "2000-01-01"
        binding.datePickerDOB.init(2000, 0, 1, null)

        binding.textViewHeight.text = "175"
        binding.editTextHeight.setText("175")
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

    private fun saveProfile() {
        val lastName = binding.editTextLastName.text.toString()
        val firstName = binding.editTextFirstName.text.toString()
        val gender = binding.spinnerGender.selectedItem.toString()
        val dob = "${binding.datePickerDOB.year}-${binding.datePickerDOB.month + 1}-${binding.datePickerDOB.dayOfMonth}"
        val height = binding.editTextHeight.text.toString()

        binding.textViewLastName.text = lastName
        binding.textViewFirstName.text = firstName
        binding.textViewDOB.text = dob
        binding.textViewHeight.text = height

        // 수정된 값들을 저장하거나 다른 곳으로 전달하는 등의 작업 수행
        Toast.makeText(this, "프로필이 저장되었습니다", Toast.LENGTH_SHORT).show()
    }

    fun onEditLastNameClick(view: View) {
        if (!isEditing) {
            binding.editTextLastName.isEnabled = true
            binding.editTextLastName.visibility = View.VISIBLE
        }
    }

    fun onEditFirstNameClick(view: View) {
        if (!isEditing) {
            binding.editTextFirstName.isEnabled = true
            binding.editTextFirstName.visibility = View.VISIBLE
        }
    }

    fun onEditGenderClick(view: View) {
        if (!isEditing) {
            binding.spinnerGender.isEnabled = true
            binding.spinnerGender.visibility = View.VISIBLE
        }
    }

    fun onEditDOBClick(view: View) {
        if (!isEditing) {
            binding.datePickerDOB.isEnabled = true
            binding.datePickerDOB.visibility = View.VISIBLE
        }
    }

    fun onEditHeightClick(view: View) {
        if (!isEditing) {
            binding.editTextHeight.isEnabled = true
            binding.editTextHeight.visibility = View.VISIBLE
        }
    }
}

