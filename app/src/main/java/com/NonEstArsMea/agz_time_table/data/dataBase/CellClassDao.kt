package com.NonEstArsMea.agz_time_table.data.dataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CellClassDao {

    @Query("SELECT * FROM cell_classes")
    fun getCellClass(): LiveData<List<CellClassDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCellClass(list: List<CellClassDbModel>)

}