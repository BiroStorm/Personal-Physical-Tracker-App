package it.lam.pptproject.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.lam.pptproject.ui.screen.ChartsScreen
import it.lam.pptproject.ui.screen.HomeScreen2
import it.lam.pptproject.ui.screen.LoginScreen
import it.lam.pptproject.ui.screen.ProfileScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {

    //val homeViewModel2: HomeViewModel2 = viewModel(factory = AppViewModelProvider.Factory)
    NavHost(
        navController = navController,
        startDestination = "Home",
        modifier = Modifier.padding(contentPadding)
    ) {
        composable("Home") { HomeScreen2() }
        composable("Charts") { ChartsScreen() }
        composable("Profile") { ProfileScreen() }
        composable("Landing") { LoginScreen() }
    }
}