package com.NonEstArsMea.agz_time_table.present.settingFragment.recycleView

sealed class RWWorkloadClass(
    val type: Int
){
    companion object{
        const val NAMING = 1
        const val DEPARTMENT_CARD = 2
    }
}
data class CafClass(
    val month: String,
    val department: String,
    val lesson: String,
    val hours: String,
): RWWorkloadClass(DEPARTMENT_CARD)

data class Naming(
    val month: String,
    val department: String,
    val lesson: String,
    val hours: String,
): RWWorkloadClass(NAMING)
