package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass

sealed class State


data object ConnectionError : State()

data object LoadData: State()

class TimeTableIsLoad(val list: List<List<CellClass>>) : State()