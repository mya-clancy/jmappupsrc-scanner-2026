package com.example.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Student
import com.example.ui.components.StatusBadge
import com.example.ui.viewmodel.MainViewModel
import com.example.util.JpgCardExporter

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.widthIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardGeneratorScreen(
    viewModel: MainViewModel,
    initialStudentId: String? = null,
    onNavigateBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val students by viewModel.filteredStudents.collectAsState()
    val currentSemester by viewModel.currentSemester.collectAsState()

    var selectedStudent by remember {
        mutableStateOf(
            students.find { it.studentId == initialStudentId }
                ?: students.firstOrNull()
                ?: Student(
                    studentId = "2026-00101-SR-0",
                    fullName = "Alex Chen",
                    email = "alex.chen@iskolarngbayan.pup.edu.ph",
                    department = "BSBA - Marketing Management",
                    activeSemester = "2026-2027 | 1st Semester",
                    isMembershipPaid = true
                )
        )
    }

    var studentDropdownExpanded by remember { mutableStateOf(false) }

    // Generate Card Bitmap Preview
    val cardBitmap: Bitmap = remember(selectedStudent) {
        JpgCardExporter.createMembershipCardBitmap(selectedStudent)
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 800.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
        // Top Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (onNavigateBack != null) {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back to Directory"
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "Card Pass Generator",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "JMAP Member Pass & QR Generator",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Render printable JMAP student ID pass & export to .jpg image",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (onNavigateBack != null) {
                        OutlinedButton(
                            onClick = onNavigateBack,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Back to Directory", fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Select Student Dropdown
                Text(
                    text = "Select Student for ID Pass:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))

                ExposedDropdownMenuBox(
                    expanded = studentDropdownExpanded,
                    onExpandedChange = { studentDropdownExpanded = !studentDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = "${selectedStudent.fullName} (${selectedStudent.studentId})",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = studentDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    ExposedDropdownMenu(
                        expanded = studentDropdownExpanded,
                        onDismissRequest = { studentDropdownExpanded = false }
                    ) {
                        students.forEach { s ->
                            DropdownMenuItem(
                                text = { Text("${s.fullName} (${s.studentId}) • ${s.department}") },
                                onClick = {
                                    selectedStudent = s
                                    studentDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Live Membership ID Card Bitmap Preview
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "High-Res Card Preview (PNG / JPG Canvas)",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Render Canvas Generated Bitmap directly
                Image(
                    bitmap = cardBitmap.asImageBitmap(),
                    contentDescription = "Membership Card Preview",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Status: ${if (selectedStudent.isMembershipPaid) "ACTIVE" else "UNPAID/EXPIRED"}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Valid Semester: ${selectedStudent.activeSemester}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    StatusBadge(
                        status = if (selectedStudent.activeSemester == currentSemester && selectedStudent.isMembershipPaid) "VALID" else "EXPIRED_SEMESTER"
                    )
                }
            }
        }

        // Export JPG Actions
        Button(
            onClick = {
                JpgCardExporter.exportAndShareJpgCard(context, selectedStudent)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Export JPG",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Export Membership Card as .JPG",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
            }
        }
    }
}
