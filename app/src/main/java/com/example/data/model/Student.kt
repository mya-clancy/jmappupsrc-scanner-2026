package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey
    val studentId: String,
    val fullName: String,
    val email: String,
    val department: String,
    val activeSemester: String,
    val isMembershipPaid: Boolean,
    val joinYear: String = "2024",
    val photoUrl: String? = null,
    val notes: String = "",
    val lastScannedAt: Long? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
