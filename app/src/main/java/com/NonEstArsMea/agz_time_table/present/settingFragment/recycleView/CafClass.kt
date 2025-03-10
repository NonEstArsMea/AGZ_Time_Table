package com.NonEstArsMea.agz_time_table.present.settingFragment.recycleView

import com.NonEstArsMea.agz_time_table.R

sealed class RWWorkloadClass(
    val type: Int
){
    companion object{
        const val MONTH = 1
        const val DEPARTMENT_CARD = 2
    }
}
data class CafClass(
    val department: String,
    val hours: String,
): RWWorkloadClass(DEPARTMENT_CARD){
    fun getColor(): Int{
        return when(department){
            "1" -> R.color.green_fo_lessons_card
            "2"-> R.color.blue_fo_lessons_card
            "3"->R.color.orange_fo_lessons_card
            else -> R.color.yellow_fo_lessons_card
        }
    }
}

data class MonthName(
    val month: String,
): RWWorkloadClass(MONTH)
