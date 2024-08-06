package it.lam.pptproject.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.lam.pptproject.R
import it.lam.pptproject.data.ButtonStateDataStore
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isTerminated by remember { mutableStateOf(false) }

    // * Recupera lo stato del pulsante all'avvio
    LaunchedEffect(Unit) {
        ButtonStateDataStore.getApplicationState(context).collect { savedState ->
            isTerminated = savedState
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                isTerminated = !isTerminated
                // * avvia una coroutine nel composable così da evitare operazioni bloccanti.
                scope.launch {
                    ButtonStateDataStore.saveApplicationState(context, isTerminated)
                }
            },
            shape = CircleShape,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Text(text = if (isTerminated) context.getString(R.string.end_btn) else context.getString(R.string.start_btn))
        }
    }
}