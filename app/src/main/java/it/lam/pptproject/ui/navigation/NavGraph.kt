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
import it.lam.pptproject.ui.screen.SearchScreen

@Composable
fun NavGraph(navController: NavHostController, contentPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(contentPadding)
    ) {
        composable("home") { HomeScreen() }
        composable("search") { SearchScreen() }
        composable("notifications") { NotificationsScreen() }
        composable("profile") { ProfileScreen() }
    }
}