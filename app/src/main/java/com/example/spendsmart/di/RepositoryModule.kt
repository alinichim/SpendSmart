package com.example.spendsmart.di

import com.example.spendsmart.data.repository.ExchangeRateRepositoryImpl
import com.example.spendsmart.data.repository.ExpenseRepositoryImpl
import com.example.spendsmart.data.repository.PreferencesRepositoryImpl
import com.example.spendsmart.domain.repository.ExchangeRateRepository
import com.example.spendsmart.domain.repository.ExpenseRepository
import com.example.spendsmart.domain.repository.PreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(impl: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    @Singleton
    abstract fun bindExchangeRateRepository(impl: ExchangeRateRepositoryImpl): ExchangeRateRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository
}
