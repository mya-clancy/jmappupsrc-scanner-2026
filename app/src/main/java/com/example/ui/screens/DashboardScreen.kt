package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ScanLog
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AmberExpired
import com.example.ui.theme.EmeraldActive

import com.example.ui.viewmodel.MainViewModel
import com.example.util.CsvExporter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.widthIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val logs by viewModel.filteredLogs.collectAsState()
    val allStudents by viewModel.filteredStudents.collectAsState()
    val logSearchQuery by viewModel.logSearchQuery.collectAsState()
    val selectedLogSemester by viewModel.selectedLogSemester.collectAsState()
    val currentSemester by viewModel.currentSemester.collectAsState()
    val eventResetTimestamp by viewModel.eventResetTimestamp.collectAsState()

    val availableSemesters = listOf(
        "All Semesters",
        "2026-2027 | 1st Semester",
        "2026-2027 | 2nd Semester",
        "2025-2026 | 1st Semester",
        "2025-2026 | 2nd Semester"
    )

    var semesterDropdownExpanded by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }
    var showUnpaidModal by remember { mutableStateOf(false) }

    // Metrics calculation
    val totalScans = if (eventResetTimestamp > 0) {
        logs.count { it.scannedAt >= eventResetTimestamp }
    } else {
        logs.size
    }

    val validScans = logs.count {
        it.status == "VALID" && (eventResetTimestamp == 0L || it.scannedAt >= eventResetTimestamp)
    }

    // Unpaid / Expired students in organization roster
    val unpaidOrExpiredStudents = allStudents.filter { !it.isMembershipPaid || it.activeSemester != currentSemester }
    val expiredPendingCount = unpaidOrExpiredStudents.size
    val totalStudentsCount = allStudents.size

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 800.dp)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
        // Header Banner & Actions
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "JMAP QR SCANNER",
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Junior Marketing Association of the Philippines • PUP Santa Rosa",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Export CSV Action Button
                        Button(
                            onClick = {
                                val file = CsvExporter.exportScanLogsToCsv(context, logs, selectedLogSemester)
                                if (file != null) {
                                    CsvExporter.shareCsvFile(
                                        context,
                                        file,
                                        "Scan History Logs - $selectedLogSemester"
                                    )
                                } else {
                                    Toast.makeText(context, "Failed to export CSV", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Export CSV",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Export CSV", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Metrics KPI Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KpiMetricCard(
                    title = if (eventResetTimestamp > 0) "Current Event Scans" else "Total Scans",
                    value = "$totalScans",
                    subtitle = if (eventResetTimestamp > 0) "Reset Active" else "Tap to reset daily",
                    icon = Icons.Default.QrCode,
                    color = MaterialTheme.colorScheme.primary,
                    onClick = { viewModel.resetDailyEventScans() },
                    modifier = Modifier.weight(1f)
                )
                KpiMetricCard(
                    title = "Valid Scans",
                    value = "$validScans",
                    subtitle = "Passed Validation",
                    icon = Icons.Default.CheckCircle,
                    color = EmeraldActive,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                KpiMetricCard(
                    title = "Expired/Pending",
                    value = "$expiredPendingCount",
                    subtitle = "Tap to view unpaid list",
                    icon = Icons.Default.HourglassTop,
                    color = AmberExpired,
                    onClick = { showUnpaidModal = true },
                    modifier = Modifier.weight(1f)
                )
                KpiMetricCard(
                    title = "JMAP Members",
                    value = "$totalStudentsCount",
                    subtitle = "Total Registered",
                    icon = Icons.Default.People,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Filters & Search Bar
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Scan Audit Trail",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ExposedDropdownMenuBox(
                            expanded = semesterDropdownExpanded,
                            onExpandedChange = { semesterDropdownExpanded = !semesterDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedLogSemester,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = semesterDropdownExpanded) },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                    .width(180.dp)
                                    .height(44.dp),
                                textStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )

                            ExposedDropdownMenu(
                                expanded = semesterDropdownExpanded,
                                onDismissRequest = { semesterDropdownExpanded = false }
                            ) {
                                availableSemesters.forEach { semester ->
                                    DropdownMenuItem(
                                        text = { Text(semester, fontSize = 13.sp) },
                                        onClick = {
                                            viewModel.setSelectedLogSemester(semester)
                                            semesterDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Clear Logs",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = logSearchQuery,
                    onValueChange = { viewModel.setLogSearchQuery(it) },
                    placeholder = { Text("Search by student name, ID, or status...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        // Logs List
        if (logs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No scan logs found for the selected filter.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(logs, key = { it.id }) { log ->
                ScanLogItemCard(log = log)
            }
        }
            }
        }
    }

    // Modal Dialog showing Expired/Pending Students list
    if (showUnpaidModal) {
        AlertDialog(
            onDismissRequest = { showUnpaidModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.HourglassTop,
                        contentDescription = null,
                        tint = AmberExpired,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Pending / Expired Members (${unpaidOrExpiredStudents.size})")
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    Text(
                        text = "Students with unpaid dues or outdated active semesters:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (unpaidOrExpiredStudents.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("All members have active paid memberships!")
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(unpaidOrExpiredStudents, key = { it.studentId }) { student ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = student.fullName,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Text(
                                                text = "ID: ${student.studentId} • ${student.email}",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontSize = 11.sp
                                            )
                                            Text(
                                                text = "Active Sem: ${student.activeSemester} | Paid: ${if (student.isMembershipPaid) "YES" else "NO"}",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }

                                        Button(
                                            onClick = {
                                                viewModel.saveStudent(
                                                    student.copy(
                                                        isMembershipPaid = true,
                                                        activeSemester = currentSemester
                                                    )
                                                )
                                                Toast.makeText(context, "Updated ${student.fullName} to PAID!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                        ) {
                                            Text("Mark Paid", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showUnpaidModal = false }) {
                    Text("Close")
                }
            }
        )
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear All Scan Logs?") },
            text = { Text("Are you sure you want to delete all historical scan logs? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearLogs()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Clear Logs")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun KpiMetricCard(
    title: String,
    value: String,
    subtitle: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onClick?.invoke() },
        enabled = onClick != null,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun ScanLogItemCard(log: ScanLog) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy • HH:mm:ss", Locale.getDefault())
    val formattedTime = dateFormat.format(Date(log.scannedAt))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = log.studentName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${log.studentId})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${log.department} • Semester: ${log.scannedSemester}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )

                if (log.notes.isNotBlank()) {
                    Text(
                        text = "Note: ${log.notes}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))
            StatusBadge(status = log.status)
        }
    }
}
