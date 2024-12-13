package com.NonEstArsMea.agz_time_table.util

import android.util.Log
import com.NonEstArsMea.agz_time_table.R

object Methods {

    fun validExams(type: String): Boolean{
        val exams = listOf("ЗаО", "э", "эк", "экз", "экз.", "Зачет", "зчО.")
        return type in exams
    }
    fun returnFullNameOfTheItemType(text: String): Int {
    return when(text){
        "ПЗ" -> R.string.type_subject_pz
        "пр." -> R.string.type_subject_pz
        "П" -> R.string.type_subject_practic
        "ЗаО" -> R.string.type_subject_credit_with_an_assessment
        "зчО." -> R.string.type_subject_credit_with_an_assessment
        "Контр.р" -> R.string.type_subject_control_work
        "Ку" -> R.string.type_subject_coursework
        "Курс" -> R.string.type_subject_coursework
        "Курс.р" -> R.string.type_subject_coursework
        "л" -> R.string.type_subject_lecture
        "л." -> R.string.type_subject_lecture
        "ЛР" -> R.string.type_subject_laboratory_work
        "с" -> R.string.type_subject_seminar
        "сем." -> R.string.type_subject_seminar
        "см" -> R.string.type_subject_seminar
        "СРПП" -> R.string.type_subject_SRPP
        "э" -> R.string.type_subject_exam
        "эк" -> R.string.type_subject_exam
        "экз" -> R.string.type_subject_exam
        "экз." -> R.string.type_subject_exam
        "ГК" -> R.string.type_subject_group_consultation
        "ГЗ" -> R.string.type_subject_group_lesson
        "конс." -> R.string.type_subject_group_consultation
        "Зачет" -> R.string.type_subject_test
        "зач." -> R.string.type_subject_test
        "лаб." -> R.string.type_subject_laboratory_work
        else -> R.string.type_subject_lesson
    }}

    fun setColor(text: String): Int{
        Log.e("col", R.color.red_fo_lessons_card.toString())
        return when(text){
            "э" -> R.color.red_fo_lessons_card
            "эк" -> R.color.red_fo_lessons_card
            "экз" -> R.color.red_fo_lessons_card
            "экз." -> R.color.red_fo_lessons_card
            "ЗаО" -> R.color.red_fo_lessons_card
            "зчО." -> R.color.red_fo_lessons_card
            "Контр.р" -> R.color.red_fo_lessons_card
            "экз." ->  R.color.red_fo_lessons_card
            "ПЗ" -> R.color.yellow_fo_lessons_card
            "пр." -> R.color.yellow_fo_lessons_card
            "сем." -> R.color.yellow_fo_lessons_card
            "П" -> R.color.yellow_fo_lessons_card
            "СРПП" -> R.color.yellow_fo_lessons_card
            "Курс.р" -> R.color.yellow_fo_lessons_card
            "ГЗ" -> R.color.yellow_fo_lessons_card
            "с" -> R.color.yellow_fo_lessons_card
            "см" -> R.color.yellow_fo_lessons_card
            "Зачет" -> R.color.yellow_fo_lessons_card
            else -> {
                R.color.green_fo_lessons_card
            }
        }
    }

}