package com.NonEstArsMea.agz_time_table.di

import androidx.lifecycle.ViewModel
import com.NonEstArsMea.agz_time_table.present.audWorkloadFragment.AudWorkloadViewModel
import com.NonEstArsMea.agz_time_table.present.cafTimeTable.CafTimeTableViewModel
import com.NonEstArsMea.agz_time_table.present.examsFragment.ExamsFragmentViewModel
import com.NonEstArsMea.agz_time_table.present.mainActivity.MainViewModel
import com.NonEstArsMea.agz_time_table.present.searchFragment.SearchViewModel
import com.NonEstArsMea.agz_time_table.present.settingFragment.SettingViewModel
import com.NonEstArsMea.agz_time_table.present.timeTableFragment.TimeTableViewModel
import com.NonEstArsMea.agz_time_table.present.workloadLayout.WorkloadViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton


@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(TimeTableViewModel::class)
    fun bindTimeTableViewModel(viewModel: TimeTableViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    fun bindSettingViewModel(viewModel: SettingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExamsFragmentViewModel::class)
    fun bindExamsViewModel(viewModel: ExamsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AudWorkloadViewModel::class)
    fun bindAudWorkloadViewModel(viewModel: AudWorkloadViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CafTimeTableViewModel::class)
    fun bindCafTimeTableViewModel(viewModel: CafTimeTableViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WorkloadViewModel::class)
    fun bindWorkloadViewModel(viewModel: WorkloadViewModel): ViewModel
}