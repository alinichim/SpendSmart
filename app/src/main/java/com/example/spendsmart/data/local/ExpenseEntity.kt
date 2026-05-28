package com.example.spendsmart.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val amount: Double,
    val currency: String,
    val category: String,
    val dateEpochDay: Long,
    val notes: String,
    val createdAt: Long
)
