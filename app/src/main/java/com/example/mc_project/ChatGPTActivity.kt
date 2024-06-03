package com.example.mc_project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
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
            sendQuestionToGPT(question, chatLayout)
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
        chatLayout.addView(textView)
    }
}
