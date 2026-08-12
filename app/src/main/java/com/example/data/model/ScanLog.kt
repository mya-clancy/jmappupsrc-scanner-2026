package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_logs")
data class ScanLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: String,
    val studentName: String,
    val department: String,
    val scannedSemester: String,
    val status: String,
    val scannedAt: Long = System.currentTimeMillis(),
    val scannedBy: String = "Admin Scanner",
    val notes: String = ""
)
