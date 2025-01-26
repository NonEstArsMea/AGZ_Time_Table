package com.NonEstArsMea.agz_time_table.present.audWorkloadFragment

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.State

sealed class WorkloadState

data object ConnectionError : WorkloadState()

data class SetDate(val date: String) : WorkloadState()

class DataIsLoad(val date: String, val unicList: List<String>, val list: List<CellClass>,val position: Int) : WorkloadState()