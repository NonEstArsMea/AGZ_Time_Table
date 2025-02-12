package com.NonEstArsMea.agz_time_table.present

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.NonEstArsMea.agz_time_table.data.storage.LocalStorageInitial
import com.NonEstArsMea.agz_time_table.data.storage.StorageRepositoryImpl
import com.NonEstArsMea.agz_time_table.di.DaggerApplicationComponent
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainActivity
import javax.inject.Inject

class TimeTableApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

}