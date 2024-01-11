package com.NonEstArsMea.agz_time_table.util

import com.NonEstArsMea.agz_time_table.R

object Methods {

    fun validExams(type: String): Boolean{
        val exams = listOf("ЗаО", "э", "эк", "экз", "экз.", "Зачет")
        return type in exams
    }
    fun returnFullNameOfTheItemType(text: String): Int {
    return when(text){
        "ПЗ" -> R.string.type_subject_pz
        "П" -> R.string.type_subject_practic
        "ЗаО" -> R.string.type_subject_credit_with_an_assessment
        "Контр.р" -> R.string.type_subject_control_work
        "Ку" -> R.string.type_subject_coursework
        "Курс" -> R.string.type_subject_coursework
        "Курс.р" -> R.string.type_subject_coursework
        "л" -> R.string.type_subject_lecture
        "ЛР" -> R.string.type_subject_laboratory_work
        "с" -> R.string.type_subject_seminar
        "см" -> R.string.type_subject_seminar
        "СРПП" -> R.string.type_subject_SRPP
        "э" -> R.string.type_subject_exam
        "эк" -> R.string.type_subject_exam
        "экз" -> R.string.type_subject_exam
        "экз." -> R.string.type_subject_exam
        "ГК" -> R.string.type_subject_group_consultation
        "ГЗ" -> R.string.type_subject_group_lesson
        "Зачет" -> R.string.type_subject_test
        else -> R.string.type_subject_lesson
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
            "Зачет" -> R.drawable.yellow_color
            else -> {
                R.drawable.green_color
            }
        }
    }

}