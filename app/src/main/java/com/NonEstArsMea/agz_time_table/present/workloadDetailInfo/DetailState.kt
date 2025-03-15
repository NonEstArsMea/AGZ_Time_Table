package com.NonEstArsMea.agz_time_table.present.workloadDetailInfo

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass

sealed class DetailState

data class DataIsLoad(
    val list: List<CellClass>,
    val name: String,
    val month: String
) : DetailState()

data class LoadData(val name: String) : DetailState()