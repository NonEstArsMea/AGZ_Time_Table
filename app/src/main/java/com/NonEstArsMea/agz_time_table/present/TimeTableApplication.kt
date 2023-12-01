package com.NonEstArsMea.agz_time_table.present

import android.app.Application
import com.NonEstArsMea.agz_time_table.di.DaggerApplicationComponent

class TimeTableApplication: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this, this)
    }

}