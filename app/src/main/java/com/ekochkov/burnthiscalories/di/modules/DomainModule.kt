package com.ekochkov.burnthiscalories.di.modules

import android.content.Context
import com.ekochkov.burnthiscalories.data.CaloriesRepository
import com.ekochkov.burnthiscalories.domain.Interactor
import com.ekochkov.burnthiscalories.util.CaloriesCalculator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule() {

    @Singleton
    @Provides
    fun provideInteractor(repository: CaloriesRepository,
                          caloriesCalculator: CaloriesCalculator,
                          context: Context) = Interactor(repository, caloriesCalculator, context)

    @Singleton
    @Provides
    fun provideCaloriesCalculator() = CaloriesCalculator()
}