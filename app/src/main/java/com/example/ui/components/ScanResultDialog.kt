package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.model.PartnerVoucher
import com.example.data.model.Student
import com.example.data.repository.ScanValidationResult
import com.example.ui.theme.AmberExpired
import com.example.ui.theme.CrimsonInvalid
import com.example.ui.theme.EmeraldActive
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultDialog(
    result: ScanValidationResult,
    currentSemester: String,
    viewModel: MainViewModel,
    onDismiss: () -> Unit,
    onRenewMembership: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    val allVouchers by viewModel.allVouchers.collectAsState()
    val loggedInPartner by viewModel.loggedInPartnerBusiness.collectAsState()

    val displayVouchers: List<PartnerVoucher> = remember(allVouchers, loggedInPartner) {
        val partner = loggedInPartner
        if (partner != null) {
            allVouchers.filter { voucher ->
                voucher.businessId == partner.id ||
                voucher.businessName.equals(partner.name, ignoreCase = true) ||
                voucher.businessId == "ALL"
            }
        } else {
            allVouchers
        }
    }

    var redeemedBusinessIds by remember { mutableStateOf<Set<String>>(emptySet()) }
    val monthYear = viewModel.currentMonthYearString()

    val currentValidStudent = (result as? ScanValidationResult.Valid)?.student

    LaunchedEffect(currentValidStudent) {
        if (currentValidStudent != null) {
            viewModel.getStudentRedeemedBusinessesThisMonth(currentValidStudent.studentId) { ids ->
                redeemedBusinessIds = ids
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (result) {
                is ScanValidationResult.Valid -> {
                    StatusHeaderIcon(
                        icon = Icons.Default.CheckCircle,
                        color = EmeraldActive,
                        title = "MEMBERSHIP VALID",
                        subtitle = "Verified for $currentSemester"
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    StudentDetailCard(
                        studentId = result.student.studentId,
                        fullName = result.student.fullName,
                        department = result.student.department,
                        activeSemester = result.student.activeSemester,
                        status = "VALID"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Voucher / Discount Selection Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ConfirmationNumber,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Redeem Monthly Partner Discount",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Text(
                                text = "Rule: Max 1 voucher per month for each partner business ($monthYear)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            if (displayVouchers.isEmpty()) {
                                Text(
                                    text = "No active vouchers distributed for this store.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            } else {
                                for (voucher in displayVouchers) {
                                    val isRedeemed = redeemedBusinessIds.contains(voucher.id)

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isRedeemed) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                                    )
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
                                                text = voucher.businessName,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = voucher.discountTitle,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }

                                        if (isRedeemed) {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = EmeraldActive.copy(alpha = 0.15f)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = EmeraldActive,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "1/1 Used",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = EmeraldActive,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        } else {
                                            Button(
                                                onClick = {
                                                    viewModel.redeemVoucher(result.student, voucher) { success, msg ->
                                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                                        if (success) {
                                                            redeemedBusinessIds = redeemedBusinessIds + voucher.id
                                                        }
                                                    }
                                                },
                                                shape = RoundedCornerShape(8.dp),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                            ) {
                                                Text("Use Discount", fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldActive)
                    ) {
                        Text("Complete Scan")
                    }
                }

                is ScanValidationResult.ExpiredSemester -> {
                    StatusHeaderIcon(
                        icon = Icons.Default.HourglassTop,
                        color = AmberExpired,
                        title = "EXPIRED SEMESTER",
                        subtitle = "Registered for ${result.student.activeSemester}, needs renewal for $currentSemester"
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    StudentDetailCard(
                        studentId = result.student.studentId,
                        fullName = result.student.fullName,
                        department = result.student.department,
                        activeSemester = result.student.activeSemester,
                        status = "EXPIRED_SEMESTER"
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            onRenewMembership(result.student.studentId)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldActive)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Autorenew,
                            contentDescription = "Renew",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Renew Membership for $currentSemester")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Dismiss")
                    }
                }

                is ScanValidationResult.PaymentPending -> {
                    StatusHeaderIcon(
                        icon = Icons.Default.Error,
                        color = AmberExpired,
                        title = "DUES PENDING",
                        subtitle = "Registered for $currentSemester but dues payment pending"
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    StudentDetailCard(
                        studentId = result.student.studentId,
                        fullName = result.student.fullName,
                        department = result.student.department,
                        activeSemester = result.student.activeSemester,
                        status = "PAYMENT_PENDING"
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            onRenewMembership(result.student.studentId)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldActive)
                    ) {
                        Text("Mark Dues Paid for $currentSemester")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Close")
                    }
                }

                is ScanValidationResult.NotFound -> {
                    StatusHeaderIcon(
                        icon = Icons.Default.Close,
                        color = CrimsonInvalid,
                        title = "STUDENT NOT FOUND",
                        subtitle = "No record matching scanned QR code (${result.scannedId})"
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = CrimsonInvalid.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Scanned ID Payload:",
                                style = MaterialTheme.typography.labelMedium,
                                color = CrimsonInvalid
                            )
                            Text(
                                text = result.scannedId,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "This card is not registered in the organization database. Please ask the student to register with the administrator.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonInvalid)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusHeaderIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(44.dp)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = color
    )
    Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun StudentDetailCard(
    studentId: String,
    fullName: String,
    department: String,
    activeSemester: String,
    status: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = fullName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "$studentId • $department",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusBadge(status = status)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Registered Semester", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(activeSemester, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}
