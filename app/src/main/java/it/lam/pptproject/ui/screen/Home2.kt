package it.lam.pptproject.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.lam.pptproject.R
import it.lam.pptproject.ui.AppViewModelProvider
import it.lam.pptproject.ui.navigation.NavigationDestination
import it.lam.pptproject.ui.viewmodel.HomeViewModel2


object HomeDestination2 : NavigationDestination {
    override val route = "Home2"
    override val name = R.string.home_screen
    override val icon = R.drawable.home_24px
}

@Composable
fun HomeScreen2(viewModel: HomeViewModel2 = viewModel(factory = AppViewModelProvider.Factory)) {
    val hasStarted = viewModel.hasStarted

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LargeStartButton(onClick = {
            viewModel.switchState()
        }, hasStarted)
    }
}

@Composable
fun LargeStartButton(onClick: () -> Unit, hasStarted: Boolean = false) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(200.dp), // Altezza del pulsante
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = if (hasStarted) stringResource(R.string.end_btn) else stringResource(R.string.start_btn),
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )
    }
}