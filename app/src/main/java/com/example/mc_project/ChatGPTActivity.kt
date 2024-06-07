package com.example.mc_project

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mc_project.models.QuestionRequest
import com.example.mc_project.models.GPTResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatGPTActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_gpt)

        addBackButton()

        val etQuestion: EditText = findViewById(R.id.etQuestion)
        val btnSend: Button = findViewById(R.id.btnSend)
        val chatLayout: LinearLayout = findViewById(R.id.chatLayout)

        btnSend.setOnClickListener {
            val question = etQuestion.text.toString()
            if (question.isNotEmpty()) {
                sendQuestionToGPT(question, chatLayout)
                etQuestion.text.clear()
            } else {
                Toast.makeText(this, "질문을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendQuestionToGPT(question: String, chatLayout: LinearLayout) {
        RetrofitInstance.api.askGPT(QuestionRequest(question)).enqueue(object : Callback<GPTResponse> {
            override fun onResponse(call: Call<GPTResponse>, response: Response<GPTResponse>) {
                if (response.isSuccessful) {
                    val answer = response.body()?.data ?: "응답을 가져올 수 없습니다."
                    addMessageToChat("나: $question", chatLayout)
                    addMessageToChat("GPT: $answer", chatLayout)
                } else {
                    addMessageToChat("GPT: 응답이 실패했습니다.", chatLayout)
                }
            }

            override fun onFailure(call: Call<GPTResponse>, t: Throwable) {
                addMessageToChat("GPT: 오류가 발생했습니다.", chatLayout)
            }
        })
    }

    private fun addMessageToChat(message: String, chatLayout: LinearLayout) {
        val textView = TextView(this).apply {
            text = message
            textSize = 16f
            setPadding(8, 8, 8, 8)
        }
        val separator = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1 // 선의 높이
            )
            setBackgroundColor(Color.BLACK) // 선의 색상
        }
        chatLayout.addView(separator)
        chatLayout.addView(textView)
    }

}
