package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partner_businesses")
data class PartnerBusiness(
    @PrimaryKey val id: String,
    val name: String,
    val category: String = "Food & Beverage",
    val pin: String = "1234"
)
