package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.BusinessesScreen
import com.example.ui.screens.CardGeneratorScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FirebaseSettingsScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.RosterScreen
import com.example.ui.screens.ScannerScreen
import com.example.ui.theme.OrgMemberQRTheme
import com.example.ui.viewmodel.AuthMode
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            OrgMemberQRTheme {
                val authMode by viewModel.authMode.collectAsState()

                when (authMode) {
                    AuthMode.LOGGED_OUT -> {
                        LoginScreen(viewModel = viewModel)
                    }
                    AuthMode.PARTNER_CASHIER -> {
                        ScannerScreen(viewModel = viewModel)
                    }
                    AuthMode.ADMIN -> {
                        val navController = rememberNavController()
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            bottomBar = {
                                NavigationBar(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 8.dp
                                ) {
                                    // 1. Dashboard
                                    val isDashboardSelected = currentRoute == "dashboard"
                                    NavigationBarItem(
                                        selected = isDashboardSelected,
                                        onClick = {
                                            navController.navigate("dashboard") {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                                        label = { Text("Dashboard") }
                                    )

                                    // 2. Directory / Roster
                                    val isRosterSelected = currentRoute == "roster" || currentRoute?.startsWith("card_gen") == true
                                    NavigationBarItem(
                                        selected = isRosterSelected,
                                        onClick = {
                                            navController.navigate("roster") {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = { Icon(Icons.Default.People, contentDescription = "Directory") },
                                        label = { Text("Directory") }
                                    )

                                    // 3. Emphasized Middle Scanner Button
                                    val isScannerSelected = currentRoute == "scanner"
                                    Box(
                                        modifier = Modifier.weight(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Surface(
                                            onClick = {
                                                navController.navigate("scanner") {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            },
                                            shape = CircleShape,
                                            color = if (isScannerSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                                            shadowElevation = 6.dp,
                                            modifier = Modifier
                                                .size(52.dp)
                                                .offset(y = (-4).dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.QrCodeScanner,
                                                    contentDescription = "Scanner",
                                                    tint = if (isScannerSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
                                                    modifier = Modifier.size(26.dp)
                                                )
                                            }
                                        }
                                    }

                                    // 4. Businesses (Partner Stores & Vouchers)
                                    val isBusinessesSelected = currentRoute == "businesses"
                                    NavigationBarItem(
                                        selected = isBusinessesSelected,
                                        onClick = {
                                            navController.navigate("businesses") {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = { Icon(Icons.Default.Storefront, contentDescription = "Businesses") },
                                        label = { Text("Businesses") }
                                    )

                                    // 5. Cloud Database
                                    val isCloudSelected = currentRoute == "firebase"
                                    NavigationBarItem(
                                        selected = isCloudSelected,
                                        onClick = {
                                            navController.navigate("firebase") {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        icon = { Icon(Icons.Default.Cloud, contentDescription = "Cloud DB") },
                                        label = { Text("Cloud DB") }
                                    )
                                }
                            }
                        ) { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = "scanner",
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable("scanner") {
                                    ScannerScreen(viewModel = viewModel)
                                }

                                composable("dashboard") {
                                    DashboardScreen(viewModel = viewModel)
                                }

                                composable("roster") {
                                    RosterScreen(
                                        viewModel = viewModel,
                                        onNavigateToCardGenerator = { studentId ->
                                            navController.navigate("card_gen?studentId=$studentId")
                                        }
                                    )
                                }

                                composable("businesses") {
                                    BusinessesScreen(viewModel = viewModel)
                                }

                                composable(
                                    route = "card_gen?studentId={studentId}",
                                    arguments = listOf(navArgument("studentId") {
                                        type = NavType.StringType
                                        nullable = true
                                        defaultValue = null
                                    })
                                ) { backStackEntry ->
                                    val studentId = backStackEntry.arguments?.getString("studentId")
                                    CardGeneratorScreen(
                                        viewModel = viewModel,
                                        initialStudentId = studentId,
                                        onNavigateBack = {
                                            if (!navController.popBackStack()) {
                                                navController.navigate("roster")
                                            }
                                        }
                                    )
                                }

                                composable("firebase") {
                                    FirebaseSettingsScreen(viewModel = viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
