package com.example.mc_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mc_project.databinding.FragmentIntermittentFastingBinding

class IntermittentFastingFragment : Fragment() {

    private var _binding: FragmentIntermittentFastingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIntermittentFastingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleTextView.text = "간헐적 단식의 장점과 방법"
        binding.advantagesTitle.text = "간헐적 단식의 장점"
        binding.advantagesContent.text = """
            1. 체중 감량
            2. 대사 건강 개선
            3. 인슐린 감수성 향상
            4. 세포 재생 및 수리 촉진
            5. 염증 감소
        """.trimIndent()
        binding.methodsTitle.text = "간헐적 단식의 방법"
        binding.methodsContent.text = """
            1. 16/8 방법: 16시간 단식, 8시간 식사
            2. 5:2 방법: 주 5일 정상 식사, 주 2일 제한 식사
            3. 24시간 단식: 주 1-2회 24시간 단식
        """.trimIndent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
