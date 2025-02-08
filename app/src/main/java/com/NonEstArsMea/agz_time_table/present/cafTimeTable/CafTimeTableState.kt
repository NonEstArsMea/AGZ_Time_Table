package com.NonEstArsMea.agz_time_table.present.cafTimeTable

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass

sealed class CafTimeTableState

data object ConnectionError : CafTimeTableState()

data class SetDate(val date: String, val id: String) : CafTimeTableState()

class DataIsLoad(
    val date: String,
    val unicList: List<String>,
    val rep: Map<String, List<CellClass>>,
    val cafName: String
) : CafTimeTableState()
