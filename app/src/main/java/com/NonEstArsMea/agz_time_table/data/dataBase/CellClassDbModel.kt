package com.NonEstArsMea.agz_time_table.data.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.NonEstArsMea.agz_time_table.R

@Entity(tableName = "cell_classes")
data class CellClassDbModel(
    var listOfCellClass: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int
)