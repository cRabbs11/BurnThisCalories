package com.ekochkov.burnthiscalories.di.modules

import android.content.Context
import com.ekochkov.burnthiscalories.data.CaloriesRepository
import com.ekochkov.burnthiscalories.domain.Interactor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideInteractor(repository: CaloriesRepository, context: Context) = Interactor(repository, context)
}