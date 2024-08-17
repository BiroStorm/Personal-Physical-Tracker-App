package it.lam.pptproject.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import it.lam.pptproject.R
import it.lam.pptproject.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val isTerminated by viewModel.isTerminated

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
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
                Text(text = "dumpa i dati")
            }
        }
    }
}