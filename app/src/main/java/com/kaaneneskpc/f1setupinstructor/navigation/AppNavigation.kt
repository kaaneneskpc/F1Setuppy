package com.kaaneneskpc.f1setupinstructor.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kaaneneskpc.f1setupinstructor.core.ui.components.GradientBackground
import com.kaaneneskpc.f1setupinstructor.feature.home.HomeScreen
import com.kaaneneskpc.f1setupinstructor.feature.history.HistoryRoute
import com.kaaneneskpc.f1setupinstructor.feature.results.setupdetails.SetupDetailsRoute

sealed class Screen(val route: String, val icon: ImageVector? = null) {
    object Home : Screen("home", Icons.Default.Home)
    object History : Screen("history", Icons.Default.Info)
    object Chatbot : Screen("chatbot", Icons.Default.List)
    object SetupDetails : Screen("setup_details/{trackName}")
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
        
        // Hide bottom bar on SetupDetails screen
        val showBottomBar = currentRoute != null && 
                           !currentRoute.startsWith("setup_details")
        
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(
                        containerColor = Color.Transparent
                    ) {
                        val currentDestination = navBackStackEntry?.destination
                        items.forEach { screen ->
                            screen.icon?.let { icon ->
                                NavigationBarItem(
                                    icon = { Icon(icon, contentDescription = null) },
                                    label = { Text(screen.route) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
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
                    /* Replace with actual ChatbotScreen */ 
                }
                
                composable(Screen.SetupDetails.route) {
                    SetupDetailsRoute(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
