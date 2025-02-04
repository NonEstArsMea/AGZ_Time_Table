package com.NonEstArsMea.agz_time_table.di

import com.NonEstArsMea.agz_time_table.data.AuthRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.TimeTableRepositoryImpl
import com.NonEstArsMea.agz_time_table.data.storage.StorageRepositoryImpl
import com.NonEstArsMea.agz_time_table.domain.AuthRepository
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.LoadData.NetUtil
import com.NonEstArsMea.agz_time_table.domain.mainUseCase.Storage.StorageRepository
import com.NonEstArsMea.agz_time_table.domain.timeTableUseCase.TimeTableRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataModule {

    @Binds
    fun bindStorageRepository(storageRepositoryImpl: StorageRepositoryImpl): StorageRepository

    @Singleton
    @Binds
    fun bindTimeTableRepository(timeTableRepositoryImpl: TimeTableRepositoryImpl): TimeTableRepository

    @Singleton
    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

}