package com.NonEstArsMea.agz_time_table.domain

import com.NonEstArsMea.agz_time_table.R

class Methods {
    fun replaceText(text: String): String {
    return when(text){
        "ПЗ" -> "Практическое занятие"
        "П" -> "Практическое занятие"
        "ЗаО" -> "Зачет с оценкой"
        "Контр.р" -> "Контрольная работа"
        "Ку" -> "Курсовая работа"
        "Курс" -> "Курсовая работа"
        "Курс.р" -> "Курсовая работа"
        "л" -> "Лекция"
        "ЛР" -> "Лабораторная работа"
        "с" -> "Семинар"
        "см" -> "Семинар"
        "СРПП" -> "СРПП"
        "э" -> "Экзамен"
        "эк" -> "Экзамен"
        "экз" -> "Экзамен"
        "экз." -> "Экзамен"
        "ГК" -> "Групповая консультация"
        "ГЗ" -> "Групповое занятие"
        else -> {text}
    }}

    fun setColor(text: String): Int{
        return when(text){
            "э" -> R.drawable.red_color
            "эк" -> R.drawable.red_color
            "экз" -> R.drawable.red_color
            "ЗаО" -> R.drawable.red_color
            "Контр.р" -> R.drawable.red_color
            "экз." ->  R.drawable.red_color
            "ПЗ" -> R.drawable.yellow_color
            "П" -> R.drawable.yellow_color
            "СРПП" -> R.drawable.yellow_color
            "Курс.р" -> R.drawable.yellow_color
            "ГЗ" -> R.drawable.yellow_color
            "с" -> R.drawable.yellow_color
            "см" -> R.drawable.yellow_color
            else -> {
                R.drawable.green_color
            }
        }
    }
}