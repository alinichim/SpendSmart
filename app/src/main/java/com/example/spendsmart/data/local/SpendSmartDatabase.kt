package com.example.spendsmart.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ExpenseEntity::class],
    version = 2,
    exportSchema = false
)
abstract class SpendSmartDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
}
