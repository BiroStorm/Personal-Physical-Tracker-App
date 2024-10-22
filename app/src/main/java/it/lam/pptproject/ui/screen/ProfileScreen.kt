package it.lam.pptproject.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import it.lam.pptproject.R
import it.lam.pptproject.ui.navigation.NavigationDestination
import it.lam.pptproject.ui.viewmodel.ProfileViewModel

object ProfileDestination : NavigationDestination {
    override val route = "Profile"
    override val name = R.string.profile_screen
    override val icon = R.drawable.account_circle_24px
}

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    val username by viewModel.activeUser

    LaunchedEffect(Unit) {
        viewModel.fetchActiveUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        username?.let {
            Text(text = "Welcome $username", style = MaterialTheme.typography.headlineMedium)
        } ?: run {
            Text(text = "No active user found")
        }
        Button(
            onClick = {
                viewModel.clearActiveUser()
                navController.navigate("landing")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Logout")
        }
    }
}