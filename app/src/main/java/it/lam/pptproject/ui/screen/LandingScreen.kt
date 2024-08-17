package it.lam.pptproject.ui.screen

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import it.lam.pptproject.R
import it.lam.pptproject.data.room.AppDatabase
import it.lam.pptproject.di.LandingViewModelFactory
import it.lam.pptproject.repository.UserRepository

import it.lam.pptproject.ui.viewmodel.LandingViewModel

@Composable
fun LandingScreen(navController: NavController) {
    // ! Senza uso di Hilt per la DI della repo nel ViewModel.
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val userRepository = UserRepository(AppDatabase.getDatabase(context).userDao())

    val viewModel: LandingViewModel = viewModel(
        factory = LandingViewModelFactory(userRepository, application)
    )

    val userName = remember { viewModel.username }

    viewModel.checkActiveUser {
        navController.navigate("home")
    }

    // IF THERE IS NOT AN ACTIVE USER
    val users = remember { viewModel.users }



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
            onClick = {
                viewModel.saveUsername()
                navController.navigate("home")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(users.value) { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.LightGray)
                ) {
                    Text(
                        text = user.username,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                viewModel.setActiveUser(user) {
                                    navController.navigate("home")
                                }
                            }
                    )
                    IconButton(
                        onClick = { viewModel.deleteUser(user) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.delete_forever_24px),
                            contentDescription = "Delete User"
                        )
                    }
                }
            }
        }
    }
}