package com.example.mc_project

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import com.example.mc_project.BaseActivity
import com.example.mc_project.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ReportActivity : BaseActivity() {
    private lateinit var tflite: Interpreter
    private lateinit var lineChart: LineChart
    private lateinit var calorieChart: LineChart
    private lateinit var carbChart: LineChart
    private lateinit var proteinChart: LineChart
    private lateinit var fatChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        addBackButton()

        // LineChart 초기화
        lineChart = findViewById(R.id.lineChart)
        calorieChart = findViewById(R.id.calorieChart)
        carbChart = findViewById(R.id.carbChart)
        proteinChart = findViewById(R.id.proteinChart)
        fatChart = findViewById(R.id.fatChart)

        // 모델 파일 로드
        tflite = Interpreter(loadModelFile())

        // 예측 수행 예제
        val input = arrayOf(floatArrayOf(1f, 2000f, 5f))
        val output = Array(1) { FloatArray(1) }
        tflite.run(input, output)

        // 결과 출력
        val predictedWeight = output[0][0]
        println("Predicted weight: $predictedWeight")

        // 예제 데이터 생성
        val weightData = listOf(90f, 88f, 89f, 88f, 87f, 89f) // 최근 6개월 몸무게 데이터
        val futureWeight = listOf(predictedWeight) // 예측된 몸무게 데이터

        // 그래프에 데이터 설정
        setChartData(weightData, futureWeight)

        // 예제 칼로리 섭취 데이터 생성 (최근 6개월)
        val calorieIntake = listOf(2000f, 2100f, 1900f, 2200f, 2300f, 2000f)
        val carbIntake = listOf(250f, 210f, 190f, 270f, 280f, 310f)
        val proteinIntake = listOf(80f, 80f, 90f, 100f, 100f, 95f)
        val fatIntake = listOf(70f, 70f, 65f, 60f, 65f, 60f)

        // 각 영양 성분별 그래프에 데이터 설정
        setCalorieChartData(calorieIntake)
        setNutrientChartData(carbChart, carbIntake, "Carbohydrates", Color.GREEN)
        setNutrientChartData(proteinChart, proteinIntake, "Protein", Color.BLUE)
        setNutrientChartData(fatChart, fatIntake, "Fat", Color.RED)

        // Summary Section 설정
        setSummary(weightData, calorieIntake)

        // Recommendations Section 설정
        setRecommendations(calorieIntake, carbIntake, proteinIntake, fatIntake)
    }

    private fun setChartData(weightData: List<Float>, futureWeight: List<Float>) {
        val entries = ArrayList<Entry>()
        val futureEntries = ArrayList<Entry>()

        // 과거 데이터 추가
        for (i in weightData.indices) {
            entries.add(Entry(i.toFloat(), weightData[i]))
        }

        // 예측 데이터 추가
        for (i in futureWeight.indices) {
            futureEntries.add(Entry((weightData.size + i).toFloat(), futureWeight[i]))
        }

        val lineDataSet = LineDataSet(entries, "Past Weight")
        lineDataSet.color = Color.BLUE
        lineDataSet.setCircleColor(Color.BLUE)

        val futureDataSet = LineDataSet(futureEntries, "Predicted Weight")
        futureDataSet.color = Color.RED
        futureDataSet.setCircleColor(Color.RED)

        val lineData = LineData(lineDataSet, futureDataSet)
        lineChart.data = lineData

        val description = Description()
        description.text = "Weight Change"
        lineChart.description = description
        lineChart.invalidate() // refresh
    }

    private fun setCalorieChartData(calorieIntake: List<Float>) {
        val entries = ArrayList<Entry>()

        // 데이터 추가
        for (i in calorieIntake.indices) {
            entries.add(Entry(i.toFloat(), calorieIntake[i]))
        }

        val calorieDataSet = LineDataSet(entries, "Total Calories")
        calorieDataSet.color = Color.MAGENTA
        calorieDataSet.setCircleColor(Color.MAGENTA)

        val lineData = LineData(calorieDataSet)
        calorieChart.data = lineData

        val description = Description()
        description.text = "Calorie Intake"
        calorieChart.description = description
        calorieChart.invalidate() // refresh
    }

    private fun setNutrientChartData(chart: LineChart, nutrientIntake: List<Float>, label: String, color: Int) {
        val entries = ArrayList<Entry>()

        // 데이터 추가
        for (i in nutrientIntake.indices) {
            entries.add(Entry(i.toFloat(), nutrientIntake[i]))
        }

        val nutrientDataSet = LineDataSet(entries, label)
        nutrientDataSet.color = color
        nutrientDataSet.setCircleColor(color)

        val lineData = LineData(nutrientDataSet)
        chart.data = lineData

        val description = Description()
        description.text = "$label Intake"
        chart.description = description
        chart.invalidate() // refresh
    }

    private fun setSummary(weightData: List<Float>, calorieIntake: List<Float>) {
        val weightChangeTextView = findViewById<TextView>(R.id.weightChangeTextView)
        val averageCalorieIntakeTextView = findViewById<TextView>(R.id.averageCalorieIntakeTextView)

        // Weight Change Summary
        val initialWeight = weightData.first() // 6개월 전의 몸무게
        val finalWeight = weightData.last() // 현재 몸무게
        val weightChange = finalWeight - initialWeight
        weightChangeTextView.text = "Weight Change: ${if (weightChange >= 0) "+" else ""}$weightChange kg (from $initialWeight kg to $finalWeight kg)"

        // Average Daily Calorie Intake Summary
        val averageCalorieIntake = calorieIntake.average()
        averageCalorieIntakeTextView.text = "Average Daily Calorie Intake: ${averageCalorieIntake.toInt()} kcal"
    }

    private fun setRecommendations(calorieIntake: List<Float>, carbIntake: List<Float>, proteinIntake: List<Float>, fatIntake: List<Float>) {
        val recommendationTextView1 = findViewById<TextView>(R.id.recommendationTextView1)
        val recommendationTextView2 = findViewById<TextView>(R.id.recommendationTextView2)

        // 예시 추천 로직 (임의로 작성)
        recommendationTextView1.text = "1. 탄수화물, 단백질, 지방의 적절한 비율로 균형 잡힌 식단을 유지하세요."
        recommendationTextView2.text = "2. 근육 유지와 성장을 지원하기 위해 단백질 섭취를 늘리는 것을 권장합니다."
    }

    @Throws(IOException::class)
    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = assets.openFd("personal_weight_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    override fun onDestroy() {
        tflite.close()
        super.onDestroy()
    }
}
