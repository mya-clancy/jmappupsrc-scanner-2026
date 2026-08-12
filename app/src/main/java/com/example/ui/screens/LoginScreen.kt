package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PartnerBusiness
import com.example.ui.theme.PupGold
import com.example.ui.theme.PupGoldContainer
import com.example.ui.theme.PupGoldDark
import com.example.ui.theme.PupMaroon
import com.example.ui.theme.PupMaroonContainer
import com.example.ui.theme.PupMaroonDark
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val businesses by viewModel.allBusinesses.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0 = Admin Login, 1 = Select Partners

    // Admin Login State
    var adminUsername by remember { mutableStateOf("") }
    var adminPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var adminErrorMessage by remember { mutableStateOf<String?>(null) }

    // Partner Store Login State
    var selectedPartner by remember { mutableStateOf<PartnerBusiness?>(null) }
    var partnerDropdownExpanded by remember { mutableStateOf(false) }
    var partnerPin by remember { mutableStateOf("") }
    var partnerErrorMessage by remember { mutableStateOf<String?>(null) }

    // Auto select first business when list loads
    if (selectedPartner == null && businesses.isNotEmpty()) {
        selectedPartner = businesses.first()
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Top Brand Icon / Logo
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(PupMaroon),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "JMAP Logo",
                        tint = PupGold,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "JMAP Membership & Partner Portal",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = PupMaroon
                )
                Text(
                    text = "PUP Santa Rosa Campus • Digital Pass System",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = PupMaroonDark
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tab Row: Admin Login vs Select Partners (PUP Maroon Header with Yellow & White text)
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = PupMaroon,
                    contentColor = PupGold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        selectedContentColor = PupGold,
                        unselectedContentColor = Color.White,
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = if (selectedTabIndex == 0) PupGold else Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Admin Login",
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedTabIndex == 0) PupGold else Color.White
                                )
                            }
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        selectedContentColor = PupGold,
                        unselectedContentColor = Color.White,
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = if (selectedTabIndex == 1) PupGold else Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Select Partners",
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedTabIndex == 1) PupGold else Color.White
                                )
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Card Container for Login Form
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (selectedTabIndex == 0) {
                            // --- ADMIN LOGIN FORM ---
                            Text(
                                text = "Organization Admin Sign In",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = PupMaroon
                            )
                            Text(
                                text = "Access student directory, roster management, & settings",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            adminErrorMessage?.let { err ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFFFEBEE),
                                        contentColor = Color(0xFFC62828)
                                    )
                                ) {
                                    Text(
                                        text = err,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            OutlinedTextField(
                                value = adminUsername,
                                onValueChange = {
                                    adminUsername = it
                                    adminErrorMessage = null
                                },
                                label = { Text("Username", color = PupMaroon) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "User",
                                        tint = PupMaroon
                                    )
                                },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = PupMaroon),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = PupMaroon,
                                    unfocusedTextColor = PupMaroon,
                                    focusedBorderColor = PupMaroon,
                                    unfocusedBorderColor = PupMaroon.copy(alpha = 0.5f),
                                    focusedLabelColor = PupMaroon,
                                    unfocusedLabelColor = PupMaroon
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = adminPassword,
                                onValueChange = {
                                    adminPassword = it
                                    adminErrorMessage = null
                                },
                                label = { Text("Password", color = PupMaroon) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Password",
                                        tint = PupMaroon
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Toggle password",
                                            tint = PupMaroon
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = PupMaroon),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = PupMaroon,
                                    unfocusedTextColor = PupMaroon,
                                    focusedBorderColor = PupMaroon,
                                    unfocusedBorderColor = PupMaroon.copy(alpha = 0.5f),
                                    focusedLabelColor = PupMaroon,
                                    unfocusedLabelColor = PupMaroon
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    val success = viewModel.loginAdmin(adminUsername, adminPassword)
                                    if (!success) {
                                        adminErrorMessage = "Invalid admin credentials! Required: User 'admin', Password 'jmapup'"
                                    } else {
                                        Toast.makeText(context, "Logged in as Admin", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PupMaroon,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = PupGold,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Login to Admin Portal", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Default Admin Login: admin / jmapup",
                                style = MaterialTheme.typography.labelSmall,
                                color = PupMaroon,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )

                        } else {
                            // --- SELECT PARTNERS / STORE CASHIER LOGIN FORM ---
                            Text(
                                text = "Partner Store Cashier Login",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = PupMaroon
                            )
                            Text(
                                text = "Select store and enter 4-digit PIN to unlock scanner",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            partnerErrorMessage?.let { err ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFFFEBEE),
                                        contentColor = Color(0xFFC62828)
                                    )
                                ) {
                                    Text(
                                        text = err,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            // Business Dropdown Selector
                            Text(
                                text = "Select your store:",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = PupMaroon,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            ExposedDropdownMenuBox(
                                expanded = partnerDropdownExpanded,
                                onExpandedChange = { partnerDropdownExpanded = !partnerDropdownExpanded },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = selectedPartner?.name ?: "No Partner Stores Found",
                                    onValueChange = {},
                                    readOnly = true,
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Business,
                                            contentDescription = "Store",
                                            tint = PupMaroon
                                        )
                                    },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = partnerDropdownExpanded) },
                                    modifier = Modifier
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                        .fillMaxWidth(),
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = PupMaroon),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = PupMaroon,
                                        unfocusedTextColor = PupMaroon,
                                        focusedBorderColor = PupMaroon,
                                        unfocusedBorderColor = PupMaroon.copy(alpha = 0.5f),
                                        focusedLabelColor = PupMaroon,
                                        unfocusedLabelColor = PupMaroon
                                    )
                                )

                                ExposedDropdownMenu(
                                    expanded = partnerDropdownExpanded,
                                    onDismissRequest = { partnerDropdownExpanded = false }
                                ) {
                                    if (businesses.isEmpty()) {
                                        DropdownMenuItem(
                                            text = { Text("No stores added yet", color = PupMaroon) },
                                            onClick = { partnerDropdownExpanded = false }
                                        )
                                    } else {
                                        businesses.forEach { business ->
                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text(business.name, fontWeight = FontWeight.Bold, color = PupMaroon)
                                                        Text(business.category, style = MaterialTheme.typography.labelSmall, color = PupMaroonDark)
                                                    }
                                                },
                                                onClick = {
                                                    selectedPartner = business
                                                    partnerDropdownExpanded = false
                                                    partnerErrorMessage = null
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Enter PIN Input Field
                            OutlinedTextField(
                                value = partnerPin,
                                onValueChange = {
                                    if (it.length <= 4) partnerPin = it
                                    partnerErrorMessage = null
                                },
                                label = { Text("Enter 4-Digit Store PIN", color = PupMaroon) },
                                placeholder = { Text("e.g. 1234", color = PupMaroon.copy(alpha = 0.5f)) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "PIN",
                                        tint = PupMaroon
                                    )
                                },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = PupMaroon),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = PupMaroon,
                                    unfocusedTextColor = PupMaroon,
                                    focusedBorderColor = PupMaroon,
                                    unfocusedBorderColor = PupMaroon.copy(alpha = 0.5f),
                                    focusedLabelColor = PupMaroon,
                                    unfocusedLabelColor = PupMaroon
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Action button styled in PUP Yellow / Gold with Dark Maroon text
                            Button(
                                onClick = {
                                    val partner = selectedPartner
                                    if (partner == null) {
                                        partnerErrorMessage = "Please select a partner store!"
                                        return@Button
                                    }
                                    if (partnerPin.isBlank()) {
                                        partnerErrorMessage = "Please enter the 4-digit PIN for ${partner.name}!"
                                        return@Button
                                    }
                                    val success = viewModel.loginPartner(partner, partnerPin)
                                    if (!success) {
                                        partnerErrorMessage = "Incorrect PIN for ${partner.name}! (Default PIN is 1234)"
                                    } else {
                                        Toast.makeText(context, "Scanner Unlocked for ${partner.name}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PupGold,
                                    contentColor = PupMaroonDark
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = PupMaroonDark
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Unlock Camera Scanner", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Default Store PIN for testing: 1234",
                                style = MaterialTheme.typography.labelSmall,
                                color = PupMaroonDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
