package it.lam.pptproject.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.lam.pptproject.ui.screen.HomeScreen
import it.lam.pptproject.ui.screen.NotificationsScreen
import it.lam.pptproject.ui.screen.ProfileScreen
import it.lam.pptproject.ui.screen.ChartsScreen
import it.lam.pptproject.ui.screen.LandingScreen

@Composable
fun NavGraph(navController: NavHostController, contentPadding: PaddingValues, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "landing",
        modifier = Modifier.padding(contentPadding)
    ) {
        composable("Home") { HomeScreen() }
        composable("Charts") { ChartsScreen() }
        composable("notifications") { NotificationsScreen() }
        composable("Profile") { ProfileScreen(navController) }
        composable("Landing") { LandingScreen(navController) }
    }
}