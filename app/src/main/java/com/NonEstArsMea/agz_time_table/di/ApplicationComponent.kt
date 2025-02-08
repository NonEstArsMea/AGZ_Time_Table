package com.NonEstArsMea.agz_time_table.di

import android.app.Application
import com.NonEstArsMea.agz_time_table.present.audWorkloadFragment.AudWorkloadFragment
import com.NonEstArsMea.agz_time_table.present.cafTimeTable.CafTimeTableFragment
import com.NonEstArsMea.agz_time_table.present.cafTimeTable.SelectItemDialog
import com.NonEstArsMea.agz_time_table.present.departmentalTimetableFragment.DepartmentalTimeTableFragment
import com.NonEstArsMea.agz_time_table.present.examsFragment.ExamsFragment
import com.NonEstArsMea.agz_time_table.present.loginLayout.LoginFragment
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainActivity
import com.NonEstArsMea.agz_time_table.present.searchFragment.SearchFragment
import com.NonEstArsMea.agz_time_table.present.settingFragment.SettingFragment
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.TableFragment
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.TimeTableFragment
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.ViewPagerFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DataModule::class, ViewModelModule::class, AppModule::class]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: TimeTableFragment)

    fun inject(fragment: SettingFragment)

    fun inject(fragment: SearchFragment)

    fun inject(fragment: ExamsFragment)

    fun inject(fragment: ViewPagerFragment)

    fun inject(fragment: TableFragment)

    fun inject(fragment: LoginFragment)

    fun inject(fragment: AudWorkloadFragment)

    fun inject(fragment: CafTimeTableFragment)

    fun inject(fragment: DepartmentalTimeTableFragment)

    fun inject(fragment: SelectItemDialog)
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
        ): ApplicationComponent

    }

}