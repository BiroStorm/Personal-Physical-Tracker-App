package it.lam.pptproject.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import it.lam.pptproject.R
import it.lam.pptproject.ui.charts.MonthlyStepsBarChart
import it.lam.pptproject.ui.charts.PieChart
import it.lam.pptproject.ui.navigation.NavigationDestination
import it.lam.pptproject.ui.viewmodel.ChartsViewModel

object ChartsDestination : NavigationDestination {
    override val route = "Charts"
    override val name = R.string.screen_charts
    override val icon = R.drawable.baseline_auto_graph_24
}

// * Collegamento diretto con il ViewModel.
@Composable
fun ChartsScreen(
    viewModel: ChartsViewModel = hiltViewModel(),
) {
    val percentageData by viewModel.percentage.observeAsState(emptyList())
    val monthlySteps by viewModel.monthlySteps.observeAsState(emptyList())


    Log.i("ChartsScreen", "monthlySteps: $monthlySteps")
    Column {
        PieChart(percentageData)

        MonthlyStepsBarChart(monthlySteps)
    }


}
