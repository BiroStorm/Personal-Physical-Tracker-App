package it.lam.pptproject

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import it.lam.pptproject.ui.screen.LoginScreen
import it.lam.pptproject.ui.viewmodel.BaseScreenViewModel
import kotlinx.coroutines.delay


@Composable
fun MainScreen(viewModel: BaseScreenViewModel = hiltViewModel()) {

    val user by viewModel.activeUser.observeAsState()

    // * Effetto di caricamento per far in modo di caricare 'user'
    var isVisible by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(500L)
        isVisible = false
    }

    if (isVisible) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator() // Cerchio che gira
        }
    } else {

        // * Reindirizzamento dopo il "finto caricamento"
        if (user == null) {
            LoginScreen()
        } else {
            InventoryNavHost()
        }
    }

}

