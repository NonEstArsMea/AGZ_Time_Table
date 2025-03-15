package com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView

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
    val hours: String,
    val depNumber: String,
    val month: String
): RWWorkloadClass(DEPARTMENT_CARD){
    fun getColor(): Int{
        return when(depNumber){
            "1" -> R.color.color_workload_100
            "2"-> R.color.color_workload_200
            "3"->R.color.color_workload_300
            "4"->R.color.color_workload_400
            "5"->R.color.color_workload_500
            "6"->R.color.color_workload_600
            else -> R.color.color_workload_100
        }
    }
}

data class MonthName(
    val month: String,
): RWWorkloadClass(MONTH)
