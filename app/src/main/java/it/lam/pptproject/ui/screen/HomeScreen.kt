package it.lam.pptproject.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import it.lam.pptproject.R
import it.lam.pptproject.ui.navigation.NavigationDestination
import it.lam.pptproject.ui.viewmodel.HomeViewModel
import it.lam.pptproject.utils.Utils


object HomeDestination : NavigationDestination {
    override val route = "Home"
    override val name = R.string.home_screen
    override val icon = R.drawable.home_24px
}

@Composable
fun HomeScreen2(viewModel: HomeViewModel = hiltViewModel()) {
    //val hasStarted = viewModel.hasStarted
    var showDialog by remember { mutableStateOf(false) }

    val isStarted by viewModel.isStarted.collectAsState(false)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LargeStartButton(onClick = {
            // * Open popup to choose the type of tracking.
            if (!isStarted!!) showDialog = true
            else viewModel.switchState()

        }, isStarted!!)
    }

    if (showDialog) {
        ChooseTrackingTypePopup(
            onDismiss = { showDialog = false },
            onAccept = { selectedOption : String ->
                viewModel.switchState()
                showDialog = false
                if(selectedOption != "AUTOMATIC")
                viewModel.startTrackingService(selectedOption)
                else viewModel.startDetectionService()
            }
        )
    }

}

@Composable
private fun ChooseTrackingTypePopup(onDismiss: () -> Unit, onAccept: (String) -> Unit) {
    // * Popup per scegliere il tipo di tracking.
    val options = Utils.RecordType.entries.map { it.name }
    var selectedOption by remember { mutableStateOf(options[0]) }  // Opzione selezionata di default

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = stringResource(R.string.select_activity_txt),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Lista di radio button
                LazyColumn {
                    items(options) { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedOption = option }  // Cambia selezione al click
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = (option == selectedOption),
                                onClick = { selectedOption = option }
                            )
                            Text(
                                text = option,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(stringResource(R.string.cancel_btn))
                    }
                    Button(
                        onClick = { onAccept(selectedOption) },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(stringResource(R.string.start_btn))
                    }
                }
            }
        }
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
