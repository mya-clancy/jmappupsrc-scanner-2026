package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberExpired
import com.example.ui.theme.EmeraldActive

@Composable
fun QuickScanTestBar(
    onScanPayload: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showManualDialog by remember { mutableStateOf(false) }
    var customPayloadInput by remember { mutableStateOf("") }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.BugReport,
                        contentDescription = "Test Scanner",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = "Quick Test Badges (Emulator / Demo)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                TextButton(onClick = { showManualDialog = !showManualDialog }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Manual ID Input",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("Manual Input", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = { onScanPayload("2026-00101-SR-0") },
                    label = { Text("Scan Alex (Valid)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = EmeraldActive
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = EmeraldActive.copy(alpha = 0.1f)
                    )
                )

                AssistChip(
                    onClick = { onScanPayload("2026-00103-SR-0") },
                    label = { Text("Scan Marcus (Expired)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.HourglassTop,
                            contentDescription = null,
                            tint = AmberExpired
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = AmberExpired.copy(alpha = 0.1f)
                    )
                )

                AssistChip(
                    onClick = { onScanPayload("2026-00105-SR-0") },
                    label = { Text("Scan David (Pending Dues)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.HourglassTop,
                            contentDescription = null,
                            tint = AmberExpired
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = AmberExpired.copy(alpha = 0.1f)
                    )
                )

                AssistChip(
                    onClick = { onScanPayload("UNKNOWN-99") },
                    label = { Text("Scan Unknown QR") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null
                        )
                    }
                )
            }

            if (showManualDialog) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = customPayloadInput,
                        onValueChange = { customPayloadInput = it },
                        placeholder = { Text("Enter Student ID e.g. STD-2026-102", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (customPayloadInput.isNotBlank()) {
                                onScanPayload(customPayloadInput)
                                customPayloadInput = ""
                                showManualDialog = false
                            }
                        }
                    ) {
                        Text("Validate")
                    }
                }
            }
        }
    }
}
