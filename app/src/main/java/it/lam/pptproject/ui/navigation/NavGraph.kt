package it.lam.pptproject.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.lam.pptproject.ui.AppViewModelProvider
import it.lam.pptproject.ui.screen.HomeScreen
import it.lam.pptproject.ui.screen.ProfileScreen
import it.lam.pptproject.ui.screen.ChartsScreen
import it.lam.pptproject.ui.screen.HomeScreen2
import it.lam.pptproject.ui.screen.LandingScreen
import it.lam.pptproject.ui.viewmodel.HomeViewModel
import it.lam.pptproject.ui.viewmodel.HomeViewModel2

@Composable
fun NavGraph(navController: NavHostController, contentPadding: PaddingValues, modifier: Modifier = Modifier) {

    val homeViewModel2: HomeViewModel2 = viewModel(factory = AppViewModelProvider.Factory)
    NavHost(
        navController = navController,
        startDestination = "landing",
        modifier = Modifier.padding(contentPadding)
    ) {
        composable("Home") { HomeScreen() }
        composable("Charts") { ChartsScreen() }
        composable("Home2") { HomeScreen2(homeViewModel2) }
        composable("Profile") { ProfileScreen(navController) }
        composable("Landing") { LandingScreen(navController) }
    }
}