package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ScanLog
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanLogDao {
    @Query("SELECT * FROM scan_logs ORDER BY scannedAt DESC")
    fun getAllLogs(): Flow<List<ScanLog>>

    @Query("SELECT * FROM scan_logs WHERE scannedSemester = :semester ORDER BY scannedAt DESC")
    fun getLogsBySemester(semester: String): Flow<List<ScanLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ScanLog)

    @Query("DELETE FROM scan_logs")
    suspend fun clearLogs()
}
