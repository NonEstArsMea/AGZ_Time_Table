package com.NonEstArsMea.agz_time_table.data.dataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CellClassDao {

    @Query("SELECT * FROM cell_classes")
    fun getCellClass():LiveData<List<List<CellClassDbModel>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCellClass(cellClassDbModel: CellClassDbModel)

    @Query("DELETE FROM cell_classes WHERE id=:_id")
    fun deleteCellClass(_id: Int)
}