package com.NonEstArsMea.agz_time_table.data.dataBase

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CellClassDbModel::class], version = 1, exportSchema = false)
@TypeConverters(CellClassListConverter::class)
abstract class DataBase: RoomDatabase() {

    abstract fun cellClassDao(): CellClassDao

    companion object{
        private var INSTANCE: DataBase? = null
        private val LOCK = Any()
        private const val DB_NAME = "DB_name"

        fun getInstance(app: Application): DataBase{
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK){
                INSTANCE?.let {
                    return it
                }
            }

            val db = Room.databaseBuilder(
                app,
                DataBase::class.java,
                DB_NAME
            ).build()

            INSTANCE = db
            return db
        }
    }
}