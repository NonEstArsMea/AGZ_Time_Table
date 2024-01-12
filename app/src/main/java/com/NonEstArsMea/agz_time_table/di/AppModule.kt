package com.NonEstArsMea.agz_time_table.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.NonEstArsMea.agz_time_table.data.storage.DataBaseInitial
import com.NonEstArsMea.agz_time_table.data.storage.LocalStorageInitial
import dagger.BindsInstance
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }


    @Provides
    fun provideResources(context: Context): Resources {
        return context.resources
    }

    @Provides
    fun bindLocalStorage(context: Context): LocalStorageInitial {
        return LocalStorageInitial(context)
    }

    @Provides
    fun bindDataBase(application: Application): DataBaseInitial {
        return DataBaseInitial(application)
    }
}