package it.lam.pptproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import it.lam.pptproject.ui.viewmodel.LandingViewModel

@Composable
fun LandingScreen(navController: NavController, viewModel: LandingViewModel = hiltViewModel()) {
    val userName = remember { viewModel.username }

    viewModel.checkActiveUser {
        navController.navigate("home")
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = userName.value,
            onValueChange = { userName.value = it },
            label = { Text("Enter your name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.saveUserName()
                navController.navigate("home")
                      },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}