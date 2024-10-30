package it.lam.pptproject.ui.screen

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import it.lam.pptproject.MainScreen
import it.lam.pptproject.R
import it.lam.pptproject.model.room.User
import it.lam.pptproject.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val users by viewModel.users.observeAsState(emptyList())

    val username = remember { mutableStateOf("") }

    val hasUserBeenChosen by viewModel.hasUserBeenChosen

    if (hasUserBeenChosen) {
        viewModel.resetUserSelection()
        MainScreen()

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username.value,
            onValueChange = {
                username.value = it
                Log.i("LandingScreen", "username: ${username.value}")
            },
            label = { Text("Enter your username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {

                val newUser = User(username.value, true)
                viewModel.insertNewUser(newUser)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(users) { user ->
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
                                viewModel.setActiveUser(user)
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