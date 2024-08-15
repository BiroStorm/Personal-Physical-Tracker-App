package it.lam.pptproject.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import it.lam.pptproject.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val activeUser by viewModel.activeUser

    LaunchedEffect(Unit) {
        viewModel.fetchActiveUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        activeUser?.let {
            Text(text = "Welcome ${it.username}", style = MaterialTheme.typography.headlineMedium)
        } ?: run {
            Text(text = "No active user found")
        }
    }
}