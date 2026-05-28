package com.example.spendsmart.di

import android.content.Context
import androidx.room.Room
import com.example.spendsmart.data.local.ExpenseDao
import com.example.spendsmart.data.local.SpendSmartDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SpendSmartDatabase =
        Room.databaseBuilder(
            context,
            SpendSmartDatabase::class.java,
            "spendsmart.db"
        ).fallbackToDestructiveMigration(true).build()

    @Provides
    fun provideExpenseDao(db: SpendSmartDatabase): ExpenseDao = db.expenseDao()
}
