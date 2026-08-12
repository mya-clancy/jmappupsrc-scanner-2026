package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voucher_redemptions")
data class VoucherRedemption(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: String,
    val studentName: String,
    val businessId: String,
    val businessName: String,
    val discountTitle: String,
    val monthYear: String,
    val redeemedAt: Long = System.currentTimeMillis()
)
