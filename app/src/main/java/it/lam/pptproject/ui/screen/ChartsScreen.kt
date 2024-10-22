package it.lam.pptproject.ui.screen

import android.graphics.drawable.Icon
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import it.lam.pptproject.R
import it.lam.pptproject.model.room.AppDatabase
import it.lam.pptproject.repository.ChartsRepository
import it.lam.pptproject.ui.AppViewModelProvider
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
    viewModel: ChartsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Chart(
        chart = lineChart(),
        model = viewModel.chartEntryModel.collectAsState().value,
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(),
    )
}
