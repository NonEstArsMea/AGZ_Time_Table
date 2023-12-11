package com.NonEstArsMea.agz_time_table.di

import com.NonEstArsMea.agz_time_table.data.DataRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.storage.StorageRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.LoadData.DataRepository
import com.NonEstArsMea.agz_time_table.domain.MainUseCase.Storage.StorageRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @Binds
    fun bindStorageRepository(storageRepositoryImpl: StorageRepositoryImpl): StorageRepository

    @Binds
    fun bindDataRepository(dataRepositoryImpl: DataRepositoryImpl): DataRepository

}