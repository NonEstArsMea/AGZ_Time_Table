package com.NonEstArsMea.agz_time_table.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.NonEstArsMea.agz_time_table.data.storage.LocalStorage
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun provideApplication(application: Application): Application {
        return application
    }

    @Provides
    fun provideResources(context: Context): Resources {
        return context.resources
    }

    @Provides
    fun bindLocalStorage(context: Context): LocalStorage {
        return LocalStorage(context)
    }
}