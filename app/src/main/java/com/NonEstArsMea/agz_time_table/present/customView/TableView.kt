package com.NonEstArsMea.agz_time_table.present.customView

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.NonEstArsMea.agz_time_table.R

class CustomTableView(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

    private val headerTextView: TextView

    init {
        headerTextView = TextView(context)
        headerTextView.text = "Header"
        headerTextView.setTypeface(null, Typeface.BOLD)
        addView(headerTextView)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // Размещение дочерних элементов (здесь можно добавить строки с данными)
        headerTextView.layout(0, 0, measuredWidth, headerTextView.measuredHeight)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChild(headerTextView, widthMeasureSpec, heightMeasureSpec)

        // Вычисление размеров для всего представления
        val desiredWidth = headerTextView.measuredWidth
        val desiredHeight = headerTextView.measuredHeight

        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(desiredHeight, heightMeasureSpec)
        )
    }
}
