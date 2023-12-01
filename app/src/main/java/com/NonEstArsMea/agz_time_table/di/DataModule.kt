package com.NonEstArsMea.agz_time_table.di

import android.content.Context
import com.NonEstArsMea.agz_time_table.data.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.DateRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.StateRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.StorageRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.DateRepository
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.DataRepository
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.StrotageRepository
import com.NonEstArsMea.agz_time_table.domain.StateRepository
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.TimeTableRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @ApplicationScope
    @Provides
    fun bindStorageRepository(context: Context): StrotageRepository{
        return StorageRepositoryImpl(context)
    }

    @ApplicationScope
    @Provides
    fun bindDataRepository(): DataRepository {
        return DataRepositoryImpl
    }

    @ApplicationScope
    @Provides
    fun bindDateRepository(): DateRepository{
        return DateRepositoryImpl
    }

    @ApplicationScope
    @Provides
    fun bindStateRepository(): StateRepository{
        return StateRepositoryImpl
    }

    @ApplicationScope
    @Provides
    fun bindTimeTableRepository(): TimeTableRepository{
        return TimeTableRepositoryImpl
    }

}