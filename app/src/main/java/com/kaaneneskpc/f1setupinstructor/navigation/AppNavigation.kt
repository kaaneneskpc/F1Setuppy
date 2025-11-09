package com.kaaneneskpc.f1setupinstructor.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kaaneneskpc.f1setupinstructor.core.ui.components.GradientBackground
import com.kaaneneskpc.f1setupinstructor.feature.chatbot.ChatRoute
import com.kaaneneskpc.f1setupinstructor.feature.home.HomeScreen
import com.kaaneneskpc.f1setupinstructor.feature.home.profile.ProfileRoute
import com.kaaneneskpc.f1setupinstructor.feature.history.HistoryRoute
import com.kaaneneskpc.f1setupinstructor.feature.results.setupdetails.SetupDetailsRoute

sealed class Screen(val route: String, val icon: ImageVector? = null, val iconRes: Int? = null, val label: String = "") {
    object Home : Screen("home", Icons.Default.Home, null, "Ana Sayfa")
    object History : Screen("history", null, com.kaaneneskpc.f1setupinstructor.core.ui.R.drawable.ic_history, "Geçmiş")
    object Chatbot : Screen("chatbot", null, com.kaaneneskpc.f1setupinstructor.core.ui.R.drawable.ic_chat, "Asistan")
    object SetupDetails : Screen("setup_details/{trackName}")
    object Profile : Screen("profile")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.History,
        Screen.Chatbot,
    )

    GradientBackground {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        
        // Hide bottom bar on SetupDetails and Profile screens
        val showBottomBar = currentRoute != null && 
                           !currentRoute.startsWith("setup_details") &&
                           currentRoute != "profile"
        
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(
                        containerColor = Color(0xFF1B0F0F).copy(alpha = 0.95f),
                        contentColor = Color.White,
                        tonalElevation = 8.dp
                    ) {
                        val currentDestination = navBackStackEntry?.destination
                        items.forEach { screen ->
                            if (screen.icon != null || screen.iconRes != null) {
                                val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                NavigationBarItem(
                                    icon = { 
                                        if (screen.icon != null) {
                                            Icon(
                                                imageVector = screen.icon, 
                                                contentDescription = screen.label,
                                                tint = if (isSelected) Color.Red else Color.Gray
                                            )
                                        } else if (screen.iconRes != null) {
                                            Icon(
                                                painter = painterResource(id = screen.iconRes),
                                                contentDescription = screen.label,
                                                tint = if (isSelected) Color.Red else Color.Gray
                                            )
                                        }
                                    },
                                    label = { 
                                        Text(
                                            text = screen.label,
                                            color = if (isSelected) Color.Red else Color.Gray
                                        ) 
                                    },
                                    selected = isSelected,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.Red,
                                        selectedTextColor = Color.Red,
                                        unselectedIconColor = Color.Gray,
                                        unselectedTextColor = Color.Gray,
                                        indicatorColor = Color.Red.copy(alpha = 0.2f)
                                    )
                                )
                            }
                        }
                    }
                }
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            NavHost(
                navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) { 
                    HomeScreen(navController = navController) 
                }
                
                composable(Screen.History.route) { 
                    HistoryRoute(
                        onBack = { navController.popBackStack() },
                        onNewSetup = { 
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                            }
                        },
                        onNavigateToSetup = { setupId ->
                            navController.navigate("setup_details/$setupId")
                        }
                    )
                }
                
                composable(Screen.Chatbot.route) { 
                    ChatRoute(
                        onBack = { navController.popBackStack() }
                    )
                }
                
                composable(Screen.SetupDetails.route) {
                    SetupDetailsRoute(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                
                composable(Screen.Profile.route) {
                    ProfileRoute(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
