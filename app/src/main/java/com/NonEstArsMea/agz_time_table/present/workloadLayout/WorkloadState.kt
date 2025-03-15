package com.NonEstArsMea.agz_time_table.present.workloadLayout

import com.NonEstArsMea.agz_time_table.present.workloadLayout.recycleView.RWWorkloadClass

sealed class WorkloadState

data class DataIsLoad(
    val list: List<RWWorkloadClass>,
    val name: String,
) : WorkloadState()

data class SetName(val name: String) : WorkloadState()