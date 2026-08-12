package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partner_vouchers")
data class PartnerVoucher(
    @PrimaryKey val id: String,
    val businessId: String,
    val businessName: String,
    val discountTitle: String,
    val description: String,
    val category: String = "Food & Beverage"
)
