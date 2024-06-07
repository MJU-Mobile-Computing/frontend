package com.example.mc_project

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import java.util.*

class CustomCalendarView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint()
    private val dates = mutableListOf<HighlightedDate>()

    data class HighlightedDate(val date: Calendar, val color: Int)

    init {
        // Optional: Add any initialization code if needed
    }

    private fun getActionBarHeight(): Int {
        val styledAttributes = context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val actionBarHeight = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()
        return actionBarHeight
    }

    fun highlightDates(diaryData: Map<String, String>, goalCalorie: Int) {
        dates.clear()
        diaryData.forEach { (dateStr, diary) ->
            Log.d("CustomCalendarView", "Parsing diary entry for date: $dateStr")
            val parts = diary.split("\n")
            val calories = parts.getOrNull(0)?.toIntOrNull()
            Log.d("CustomCalendarView", "Calories: $calories")
            if (calories == null) {
                Log.d("CustomCalendarView", "Invalid calories data for $dateStr")
                return@forEach
            }
            val cleanDateStr = dateStr.replace(".txt", "")
            val dateParts = cleanDateStr.split("-")
            if (dateParts.size != 3) {
                Log.d("CustomCalendarView", "Invalid date format for $cleanDateStr")
                return@forEach
            }
            val year = dateParts[0].toIntOrNull()
            val month = dateParts[1].toIntOrNull()
            val day = dateParts[2].toIntOrNull()
            if (year == null || month == null || day == null) {
                Log.d("CustomCalendarView", "Invalid date components for $cleanDateStr")
                return@forEach
            }
            val calendar = Calendar.getInstance().apply {
                set(year, month - 1, day)
            }
            val color = if (calories <= goalCalorie) Color.GREEN else Color.RED
            dates.add(HighlightedDate(calendar, color))
        }
        Log.d("CustomCalendarView", "highlightDates called with ${dates.size} dates")
        invalidate() // 뷰를 다시 그리도록 요청
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom
        val actionBarHeight = getActionBarHeight()

        val cellWidth = (width - paddingLeft - paddingRight) / 7
        val cellHeight = (height - paddingTop - paddingBottom - actionBarHeight) / 6
        Log.d("CustomCalendarView", "onDraw called")

        dates.forEach { highlightedDate ->
            paint.color = highlightedDate.color
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 5f

            val dayOfMonth = highlightedDate.date.get(Calendar.DAY_OF_MONTH)
            val firstDayOfWeek = highlightedDate.date.apply {
                set(Calendar.DAY_OF_MONTH, 1)
            }.get(Calendar.DAY_OF_WEEK)

            // Adjust column index to start from 0
            val column = (dayOfMonth + firstDayOfWeek - 2) % 7
            // Calculate row based on the week of the month and the offset caused by the first day of the month
            val row = (dayOfMonth + firstDayOfWeek - 2) / 7

            val x = paddingLeft + column * cellWidth + cellWidth / 2f
            val y = paddingTop + actionBarHeight + row * cellHeight + cellHeight / 2f

            // 빈 동그라미 그리기
            val radius = cellWidth / 3.5f
            canvas.drawCircle(x, y, radius, paint)

            // 또는 빈 네모 그리기
            // val rectSize = cellWidth / 3.5f
            // canvas.drawRect(x - rectSize, y - rectSize, x + rectSize, y + rectSize, paint)
        }
    }
}
