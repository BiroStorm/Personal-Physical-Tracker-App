package it.lam.pptproject

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import it.lam.pptproject.ui.screen.LoginScreen
import it.lam.pptproject.ui.viewmodel.BaseScreenViewModel


@Composable
fun MainScreen (viewModel: BaseScreenViewModel = hiltViewModel()){

    /*

    var isUserActive by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        isUserActive = viewModel.thereIsAnActiveUser()
        Log.i("BaseScreen", "isUserActive1: $isUserActive")

        delay(1000L) // Replace with actual loading logic

    }
     */

    val user by viewModel.activeUser.observeAsState()

    WelcomeScreen()

    Log.i("MainScreen", "user: $user")
    if (user == null) {

        Log.i("MainScreen", "user: 0")
        LoginScreen()
    } else {

        Log.i("MainScreen", "user: 1")
        InventoryNavHost()
    }
}

@Composable
private fun WelcomeScreen() {
    // TODO Fare una specie di welcome che dura qualche secondo
    // tempo che user trovi o meno l'utente.

}
