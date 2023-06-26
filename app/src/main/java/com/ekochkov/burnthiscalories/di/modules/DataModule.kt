package com.ekochkov.burnthiscalories.di.modules

import android.content.Context
import androidx.room.Room
import com.ekochkov.burnthiscalories.data.AppDataBase
import com.ekochkov.burnthiscalories.data.CaloriesRepository
import com.ekochkov.burnthiscalories.data.dao.ProfileDao
import com.ekochkov.burnthiscalories.util.CaloriesCalculator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule(val context: Context) {


    @Provides
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun provideProfileDao(): ProfileDao {
        val database = Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            AppDataBase.DATABASE_NAME
        ).build().profileDao()
        return database
    }

    @Singleton
    @Provides
    fun provideCaloriesRepository(
        profileDao: ProfileDao): CaloriesRepository = CaloriesRepository(context, profileDao)
}