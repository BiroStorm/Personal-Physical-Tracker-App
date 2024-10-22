package it.lam.pptproject.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import it.lam.pptproject.R
import it.lam.pptproject.ui.AppViewModelProvider
import it.lam.pptproject.ui.charts.PieChart
import it.lam.pptproject.ui.navigation.NavigationDestination
import it.lam.pptproject.ui.viewmodel.ChartsViewModel

object ChartsDestination : NavigationDestination {
    override val route = "Charts"
    override val name = R.string.screen_charts
    override val icon = R.drawable.baseline_auto_graph_24
}


// ! SENZA USO DI HILT
// * Collegamento diretto con il ViewModel.
@Composable
fun ChartsScreen(
    viewModel: ChartsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    val percentageData by viewModel.percentage.observeAsState(emptyList())
    PieChart(percentageData)
}
