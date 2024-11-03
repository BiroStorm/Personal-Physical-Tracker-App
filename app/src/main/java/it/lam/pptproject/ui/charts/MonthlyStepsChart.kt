package it.lam.pptproject.ui.charts

import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import it.lam.pptproject.R
import it.lam.pptproject.data.room.MonthlySteps

@Composable
fun MonthlyStepsBarChart(monthlySteps: List<MonthlySteps>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.steps_per_month_txt),
            style = TextStyle.Default,
            fontFamily = FontFamily.Default,
            fontStyle = FontStyle.Normal,
            fontSize = 20.sp
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Crossfade(targetState = monthlySteps, label = "") { monthlySteps: List<MonthlySteps> ->

                AndroidView(
                    factory = { context ->
                        val entries: List<BarEntry> =
                            monthlySteps.mapIndexed { index, monthlyStep ->
                                BarEntry(index.toFloat(), monthlyStep.totalSteps.toFloat())
                            }

                        val chart = BarChart(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            this.description.isEnabled = false
                            axisRight.isEnabled = false // Disabilita l'asse destro
                            axisLeft.axisMinimum = 0f // Imposta un limite minimo per l'asse Y

                            val maxSteps = monthlySteps.maxOfOrNull { it.totalSteps } ?: 0

                            axisLeft.axisMaximum = maxSteps.toFloat() + 10f

                            xAxis.apply {
                                position = XAxis.XAxisPosition.BOTTOM
                                granularity = 1f // Imposta l'intervallo a 1
                                setDrawGridLines(false) // Rimuovi le linee di griglia sull'asse X
                                valueFormatter = IndexAxisValueFormatter(
                                    monthlySteps.map { it.month } // Usa i nomi dei mesi come etichette
                                )
                            }

                            this.setFitBars(true)

                            invalidate()
                        }

                        val dataSet = BarDataSet(entries, "").apply {
                            colors = listOf(Color.CYAN, Color.BLUE, Color.RED) // Colori delle barre
                            valueTextSize = 10f // Dimensione del testo dei valori sulle barre
                            setDrawValues(true)
                        }

                        chart.data = BarData(dataSet).apply {
                            barWidth = 0.5f
                        }

                        chart
                    },
                    // on below line we are specifying modifier
                    // for it and specifying padding to it.
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(5.dp)
                )
            }
        }
    }

}
