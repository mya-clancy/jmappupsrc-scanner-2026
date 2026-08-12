package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.data.model.ScanLog
import com.example.data.model.Student
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExporter {

    fun exportScanLogsToCsv(context: Context, logs: List<ScanLog>, semesterFilter: String): File? {
        return try {
            val fileName = "Scan_Logs_${semesterFilter.replace(" ", "_")}_${System.currentTimeMillis()}.csv"
            val file = File(context.cacheDir, fileName)
            val writer = FileWriter(file)

            // Header
            writer.append("Log ID,Student ID,Student Name,Department,Scanned Semester,Status,Timestamp,Scanned By,Notes\n")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            for (log in logs) {
                val formattedTime = dateFormat.format(Date(log.scannedAt))
                writer.append("\"${log.id}\",")
                    .append("\"${escapeCsv(log.studentId)}\",")
                    .append("\"${escapeCsv(log.studentName)}\",")
                    .append("\"${escapeCsv(log.department)}\",")
                    .append("\"${escapeCsv(log.scannedSemester)}\",")
                    .append("\"${escapeCsv(log.status)}\",")
                    .append("\"${formattedTime}\",")
                    .append("\"${escapeCsv(log.scannedBy)}\",")
                    .append("\"${escapeCsv(log.notes)}\"\n")
            }

            writer.flush()
            writer.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun exportRosterToCsv(context: Context, students: List<Student>): File? {
        return try {
            val fileName = "Student_Roster_${System.currentTimeMillis()}.csv"
            val file = File(context.cacheDir, fileName)
            val writer = FileWriter(file)

            writer.append("Student ID,Full Name,Email,Department,Active Semester,Membership Paid,Join Year,Notes\n")

            for (s in students) {
                writer.append("\"${escapeCsv(s.studentId)}\",")
                    .append("\"${escapeCsv(s.fullName)}\",")
                    .append("\"${escapeCsv(s.email)}\",")
                    .append("\"${escapeCsv(s.department)}\",")
                    .append("\"${escapeCsv(s.activeSemester)}\",")
                    .append("\"${if (s.isMembershipPaid) "Yes" else "No"}\",")
                    .append("\"${escapeCsv(s.joinYear)}\",")
                    .append("\"${escapeCsv(s.notes)}\"\n")
            }

            writer.flush()
            writer.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun shareCsvFile(context: Context, file: File, title: String) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(intent, "Export & Share CSV"))
    }

    private fun escapeCsv(value: String): String {
        return value.replace("\"", "\"\"")
    }
}
