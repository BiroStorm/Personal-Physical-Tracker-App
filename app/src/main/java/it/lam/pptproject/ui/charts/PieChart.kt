package it.lam.pptproject.ui.charts

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import it.lam.pptproject.R
import it.lam.pptproject.data.room.TypePercentageData
import it.lam.pptproject.ui.theme.blueColor
import it.lam.pptproject.ui.theme.greenColor
import it.lam.pptproject.ui.theme.redColor
import it.lam.pptproject.ui.theme.yellowColor


@Composable
fun PieChart(pieChartData: List<TypePercentageData>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.percentage_for_actvity_txt),
            style = TextStyle.Default,
            fontFamily = FontFamily.Default,
            fontStyle = FontStyle.Normal,
            fontSize = 20.sp
        )

        Column(
            modifier = Modifier
                .padding(18.dp)
                .size(320.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Crossfade(targetState = pieChartData, label = "") { pieChartData ->
                // on below line we are creating an
                // android view for pie chart.
                AndroidView(factory = { context ->
                    PieChart(context).apply {
                        // *
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        this.description.isEnabled = false
                        this.isDrawHoleEnabled = false
                        // * change to true if you want to show the legend
                        this.legend.isEnabled = false
                        this.legend.textSize = 14F
                        this.legend.horizontalAlignment =
                            Legend.LegendHorizontalAlignment.CENTER
                        ContextCompat.getColor(context, R.color.white)
                    }
                },
                    // on below line we are specifying modifier
                    // for it and specifying padding to it.
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(5.dp), update = {
                        updatePieChartWithData(it, pieChartData)
                    })
            }
        }
    }


}

fun updatePieChartWithData(
    chart: PieChart,
    data: List<TypePercentageData>,
) {
    val entries = ArrayList<PieEntry>()

    for (i in data.indices) {
        val item = data[i]
        entries.add(PieEntry(item.percentage ?: 0.toFloat(), item.type ?: ""))
    }

    val ds = PieDataSet(entries, "")

    ds.colors = arrayListOf(
        greenColor.toArgb(),
        blueColor.toArgb(),
        redColor.toArgb(),
        yellowColor.toArgb(),
    )

    ds.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
    ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    ds.sliceSpace = 4f

    ds.valueTextColor = R.color.white

    ds.valueTextSize = 20f
    ds.valueTypeface = Typeface.DEFAULT_BOLD

    val d = PieData(ds)
    chart.data = d

    //* permette di ridisegnare il grafico
    chart.invalidate()
}