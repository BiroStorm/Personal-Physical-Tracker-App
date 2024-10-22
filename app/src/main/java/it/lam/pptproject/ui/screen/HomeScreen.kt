package it.lam.pptproject.ui.screen

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.lam.pptproject.R
import it.lam.pptproject.di.HomeViewModelFactory
import it.lam.pptproject.repository.TrackingRepository
import it.lam.pptproject.ui.navigation.NavigationDestination
import it.lam.pptproject.ui.viewmodel.HomeViewModel
import it.lam.pptproject.utils.Tracker


object HomeDestination : NavigationDestination {
    override val route = "Home"
    override val name = R.string.home_screen
    override val icon = R.drawable.home_24px
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val context = LocalContext.current

    val application = context.applicationContext as Application

    val trackingRepository = TrackingRepository(context)
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(trackingRepository, application)
    )

    val isTerminated by viewModel.isTerminated
    val selectedType by viewModel.currentType


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            // https://www.composables.com/material3/exposeddropdownmenubox
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { if (!isTerminated) expanded = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp),

            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    value = selectedType.name,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    label = { Text("Status Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    Tracker.RecordType.entries.forEach() { option ->
                        DropdownMenuItem(
                            text = { Text(option.name, style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                viewModel.updateTrackerType(option)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
            Button(
                onClick = {
                    viewModel.toggleState()
                },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            ) {
                Text(text = if (isTerminated) context.getString(R.string.end_btn) else context.getString(R.string.start_btn))
            }
            Button(
                onClick = {
                    viewModel.readFitnessData()
                },
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            ) {
                Text(text = "Salva i Dati")
            }
        }
    }
}
