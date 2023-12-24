package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellApi

sealed class State

class InitialFragment(val mainParam: String) : State()

data object ConnectionError : State()

data object LoadTimeTable : State()

class TimeTableIsLoad(val list: List<List<CellApi>>) : State()