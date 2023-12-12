package com.NonEstArsMea.agz_time_table.di

import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.net.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.storage.StorageRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.DataRepository
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.StorageRepository
import com.NonEstArsMea.agz_time_table.domain.TimeTableUseCase.TimeTableRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataModule {

    @Binds
    fun bindStorageRepository(storageRepositoryImpl: StorageRepositoryImpl): StorageRepository

    @Singleton
    @Binds
    fun bindDataRepository(dataRepositoryImpl: DataRepositoryImpl): DataRepository

    @Singleton
    @Binds
    fun bindTimeTableRepository(timeTableRepositoryImpl: TimeTableRepositoryImpl): TimeTableRepository

}