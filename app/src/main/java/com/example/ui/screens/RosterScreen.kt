package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Student
import com.example.ui.components.StatusBadge
import com.example.ui.viewmodel.MainViewModel
import com.example.util.CsvExporter
import com.example.util.JpgCardExporter

@Composable
fun RosterScreen(
    viewModel: MainViewModel,
    onNavigateToCardGenerator: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val students by viewModel.filteredStudents.collectAsState()
    val searchQuery by viewModel.rosterSearchQuery.collectAsState()
    val filterPaidOnly by viewModel.rosterFilterPaidOnly.collectAsState()
    val currentSemester by viewModel.currentSemester.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var editingStudent by remember { mutableStateOf<Student?>(null) }
    var studentToDelete by remember { mutableStateOf<Student?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Student Member")
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isTablet = maxWidth > 600.dp

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn(
                    modifier = Modifier
                        .widthIn(max = 800.dp)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 88.dp, top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header Bar
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "JMAP Student Directory",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${students.size} BSBA Marketing Management members",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                // Compact Export CSV Button
                                Button(
                                    onClick = {
                                        val file = CsvExporter.exportRosterToCsv(context, students)
                                        if (file != null) {
                                            CsvExporter.shareCsvFile(context, file, "JMAP Student Directory CSV")
                                        } else {
                                            Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = "Export Roster",
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Export CSV", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Search Bar & Filter Chips
                    item {
                        Column {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.setRosterSearchQuery(it) },
                                placeholder = { Text("Search by student name or ID...", fontSize = 13.sp) },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilterChip(
                                    selected = filterPaidOnly == null,
                                    onClick = { viewModel.setRosterFilter(null) },
                                    label = { Text("All Members", fontSize = 12.sp) }
                                )

                                FilterChip(
                                    selected = filterPaidOnly == true,
                                    onClick = { viewModel.setRosterFilter(true) },
                                    label = { Text("Active Paid", fontSize = 12.sp) }
                                )

                                FilterChip(
                                    selected = filterPaidOnly == false,
                                    onClick = { viewModel.setRosterFilter(false) },
                                    label = { Text("Unpaid / Expired", fontSize = 12.sp) }
                                )
                            }
                        }
                    }

                    // Student Roster Cards
                    if (students.isEmpty()) {
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
                                        text = "No student records match your search query.",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    } else {
                        items(students, key = { it.studentId }) { student ->
                            StudentRosterItemCard(
                                student = student,
                                currentSemester = currentSemester,
                                onEdit = { editingStudent = student },
                                onDelete = { studentToDelete = student },
                                onGenerateCard = { onNavigateToCardGenerator(student.studentId) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add or Edit Student Dialog
    if (showAddDialog || editingStudent != null) {
        StudentFormDialog(
            studentToEdit = editingStudent,
            defaultSemester = currentSemester,
            onDismiss = {
                showAddDialog = false
                editingStudent = null
            },
            onSave = { student ->
                viewModel.saveStudent(student)
                showAddDialog = false
                editingStudent = null
                Toast.makeText(context, "Student record saved!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Delete Confirmation Dialog
    studentToDelete?.let { student ->
        AlertDialog(
            onDismissRequest = { studentToDelete = null },
            title = {
                Text("Remove Student Member?")
            },
            text = {
                Text(
                    text = "Are you sure you want to remove ${student.fullName} (${student.studentId}) from the directory? This action will remove their local record.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteStudent(student.studentId)
                        studentToDelete = null
                        Toast.makeText(context, "Removed ${student.fullName}", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete Student")
                }
            },
            dismissButton = {
                TextButton(onClick = { studentToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun StudentRosterItemCard(
    student: Student,
    currentSemester: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onGenerateCard: () -> Unit
) {
    val status = when {
        student.activeSemester == currentSemester && student.isMembershipPaid -> "VALID"
        student.activeSemester == currentSemester && !student.isMembershipPaid -> "PAYMENT_PENDING"
        else -> "EXPIRED_SEMESTER"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Row: [Top-Leftmost QR Icon Button] + [Student Name & Info] + [Status Badge]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Top-Leftmost QR Button (Fixed consistent size 40x40dp)
                    Surface(
                        onClick = onGenerateCard,
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "QR Pass for ${student.fullName}",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = student.fullName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "ID: ${student.studentId}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Compact Clean Status Badge
                StatusBadge(status = status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Member Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Dept: ${student.department}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Email: ${student.email}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Semester: ${student.activeSemester}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons Row (Edit, Delete, QR Pass)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    OutlinedButton(
                        onClick = onEdit,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = onDelete,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Remove", fontSize = 11.sp)
                    }
                }

                Button(
                    onClick = onGenerateCard,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("QR Pass", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudentFormDialog(
    studentToEdit: Student?,
    defaultSemester: String,
    onDismiss: () -> Unit,
    onSave: (Student) -> Unit
) {
    var studentId by remember { mutableStateOf(studentToEdit?.studentId ?: "2026-${(10000..99999).random()}-SR-0") }
    var fullName by remember { mutableStateOf(studentToEdit?.fullName ?: "") }
    var email by remember { mutableStateOf(studentToEdit?.email ?: "") }
    var activeSemester by remember { mutableStateOf(studentToEdit?.activeSemester ?: defaultSemester) }
    var isPaid by remember { mutableStateOf(studentToEdit?.isMembershipPaid ?: true) }
    var notes by remember { mutableStateOf(studentToEdit?.notes ?: "") }

    val semesterOptions = listOf(
        "2026-2027 | 1st Semester",
        "2026-2027 | 2nd Semester",
        "2025-2026 | 1st Semester",
        "2025-2026 | 2nd Semester"
    )
    var semesterExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (studentToEdit == null) "Add JMAP Member Record" else "Edit JMAP Member Record") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = studentId,
                    onValueChange = { studentId = it },
                    label = { Text("Student ID Number") },
                    placeholder = { Text("e.g. 2026-00101-SR-0") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = studentToEdit == null
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    placeholder = { Text("First Name Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("School Email Account") },
                    placeholder = { Text("student@iskolarngbayan.pup.edu.ph") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = semesterExpanded,
                    onExpandedChange = { semesterExpanded = !semesterExpanded }
                ) {
                    OutlinedTextField(
                        value = activeSemester,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Active Semester") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = semesterExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = semesterExpanded,
                        onDismissRequest = { semesterExpanded = false }
                    ) {
                        semesterOptions.forEach { sem ->
                            DropdownMenuItem(
                                text = { Text(sem) },
                                onClick = {
                                    activeSemester = sem
                                    semesterExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = isPaid,
                        onCheckedChange = { isPaid = it }
                    )
                    Text("Membership Dues Paid ($activeSemester)")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (fullName.isNotBlank() && studentId.isNotBlank()) {
                        val student = Student(
                            studentId = studentId.trim(),
                            fullName = fullName.trim(),
                            email = email.trim(),
                            department = "BSBA - Marketing Management",
                            activeSemester = activeSemester,
                            isMembershipPaid = isPaid,
                            notes = notes.trim(),
                            updatedAt = System.currentTimeMillis()
                        )
                        onSave(student)
                    }
                }
            ) {
                Text("Save Member")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
