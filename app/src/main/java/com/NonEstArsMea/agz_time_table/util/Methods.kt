package com.NonEstArsMea.agz_time_table.util

import android.util.Log
import android.widget.TextView
import com.NonEstArsMea.agz_time_table.R

object Methods {

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

fun String.getFullName(): String {
    return when(this){
        "1" -> "ФРС"
        "2" -> "КИФ"
        "3" -> "Ф(И)"
        "4" -> "Ф(Г)"
        "5" -> "ФЗО"
        "6" -> "Ф(ПИС)"
        else -> "Неизвестное подразделение"
    }
}

fun TextView.animateSlideText(newText: String) {
    this.animate()
        .translationY(-5f)
        .alpha(0f)
        .setDuration(200)
        .withEndAction {
            this.text = newText
            this.translationY = 5f
            this.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(200)
                .start()
        }
        .start()
}