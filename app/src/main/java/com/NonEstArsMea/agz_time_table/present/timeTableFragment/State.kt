package com.NonEstArsMea.agz_time_table.present.timeTableFragment

import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass

sealed class State


data object LoadData: State()

/**
* List - само расписание
* day - день недели для установки во вью пейджере
*/

class TimeTableIsLoad(val list: List<List<CellClass>>, val day: Int) : State()

class StorageLoad(val list: List<List<CellClass>>, val day: Int) : State()
