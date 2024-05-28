package com.NonEstArsMea.agz_time_table.data.storage

import android.content.Context
import android.util.Log
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.inject.Inject

class DataStorageManager @Inject constructor(
    private val context: Context
) {

    fun saveData(list: List<List<CellClass>>?) {
        if (list != null) {
            try {
                val fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
                val oos = ObjectOutputStream(fos)
                oos.writeObject(list)
                oos.close()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun loadData(): List<List<CellClass>> {
        var yourList: List<List<CellClass>>? = null

        try {
            val fis = context.openFileInput(FILE_NAME)
            val ois = ObjectInputStream(fis)
            yourList = ois.readObject() as List<List<CellClass>>?
            ois.close()
            fis.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Log.e("storrage_load", yourList.toString())
        return yourList ?: emptyList()
    }

    private companion object{
        const val FILE_NAME = "file_name"
    }
}