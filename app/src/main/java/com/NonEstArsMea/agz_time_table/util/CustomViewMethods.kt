package com.NonEstArsMea.agz_time_table.util

import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

fun getStaticLayout(
    text: String,
    width: Float,
    paint: TextPaint,
    alignLeft: Boolean = false
): StaticLayout {
    val align = if (alignLeft) {
        Layout.Alignment.ALIGN_NORMAL
    } else {
        Layout.Alignment.ALIGN_CENTER
    }
    return StaticLayout.Builder.obtain(
        /* source = */ text,
        /* start = */  0,
        /* end = */    text.length,
        /* paint = */  paint,
        /* width = */  width.toInt()
    )
        .setAlignment(align)
        .setLineSpacing(0f, 0.9f)
        .setIncludePad(true)
        .build()
}

    /**
     * Разбивает текст на строки с учетом максимальной ширины.
     *
     * @param text Исходный текст.
     * @param maxWidth Максимальная ширина строки в пикселях.
     * @param paint Объект Paint для измерения ширины текста.
     * @return Список строк, каждая из которых укладывается в указанную ширину.
     */
    fun wrapText(text: String, maxWidth: Float, paint: Paint): List<String> {
        val lines = mutableListOf<String>() // Список для строк
        var currentLine = "" // Текущая формируемая строка

        // Разбиваем текст на слова
        val words = text.split(" ")

        for (word in words) {
            // Пробуем добавить слово в текущую строку
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val textWidth = paint.measureText(testLine)

            // Если строка помещается в maxWidth, оставляем её
            if (textWidth <= maxWidth) {
                currentLine = testLine
            } else {
                // Иначе добавляем текущую строку в список и начинаем новую
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine)
                }
                currentLine = word
            }
        }

        // Добавляем последнюю строку, если она не пустая
        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }

        return lines
    }