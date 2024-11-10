package it.lam.pptproject.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import it.lam.pptproject.R
import it.lam.pptproject.data.room.TrackingDataWithDate
import it.lam.pptproject.ui.navigation.NavigationDestination
import it.lam.pptproject.ui.viewmodel.CalendarViewModel
import it.lam.pptproject.utils.Utils
import java.text.DateFormat
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


object CalendarDestination : NavigationDestination {
    override val route = "Calendar"
    override val name = R.string.calendar_screen
    override val icon = R.drawable.baseline_calendar_month_24
}

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = hiltViewModel()) {

    val activityTypes = viewModel.selectedActivityTypes.collectAsState()
    val activitiesByDate by viewModel.activitiesByDate.collectAsState()
    val activityDates = activitiesByDate.keys.sorted()

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    // Stato per memorizzare le attività della data selezionata
    var selectedActivities by remember { mutableStateOf<List<TrackingDataWithDate>?>(null) }

    // Stato per tenere traccia del mese corrente
    val visibleMonth by remember { derivedStateOf { state.firstVisibleMonth.yearMonth } }


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = visibleMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Filtro Popup
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FilterPopupButton(
                selectedTypes = activityTypes.value,
                onFilterChange = {
                    viewModel.setActivityFilter(it)
                    selectedActivities = null
                }
            )
        }

        HorizontalCalendar(
            state = state,
            monthHeader = {
                DaysOfWeekTitle(daysOfWeek = daysOfWeek)
            },
            dayContent = { day ->
                val activitiesForDate = activitiesByDate[day.date]
                Day(
                    day, activitiesForDate,
                    onClick = {
                        selectedActivities = activitiesForDate
                    }
                )
            }
        )
        selectedActivities?.let { activities ->
            OnDaySelected(activities)
        }
    }
}

@Composable
fun OnDaySelected(listOfActivity: List<TrackingDataWithDate>) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(items = listOfActivity, key = { it.id }) { activity ->
            ActivityItem(activity)
        }
    }
}

@Composable
fun ActivityItem(activity: TrackingDataWithDate) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Tipo: ${activity.type}")
            Text(text = "Inizio: ${DateFormat.getDateTimeInstance().format(activity.startTime)}")
            Text(text = "Fine: ${DateFormat.getDateTimeInstance().format(activity.endTime)}")
            Text(text = "Passi: ${activity.steps}")
        }
    }
}

@Composable
fun Day(
    day: CalendarDay,
    listOfActivity: List<TrackingDataWithDate>?,
    onClick: (CalendarDay) -> Unit,
) {

    // * Verifica se ci sono attività per questa data
    val hasActivity = listOfActivity?.isNotEmpty() == true

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(4.dp)
            .size(48.dp)
            .background(
                color = if (hasActivity) Color.Blue else Color.DarkGray,
                shape = CircleShape
            )
            .clickable {
                onClick(day)

            }
    ) {
        Text(
            text = "" + day.date.dayOfMonth,
            color = if (day.position == DayPosition.MonthDate) Color.White else Color.Gray
        )
    }
}

@Composable
fun FilterPopupButton(
    selectedTypes: List<Utils.RecordType>,
    onFilterChange: (List<Utils.RecordType>) -> Unit,
) {
    var isDialogVisible by remember { mutableStateOf(false) }

    // Stato per tenere traccia delle attività selezionate nel filtro
    var selectedFilterTypes by remember { mutableStateOf(selectedTypes) }

    val showDialog = { isDialogVisible = true }

    val dismissDialog = { isDialogVisible = false }

    IconButton(onClick = showDialog) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_filter_alt_24),
            contentDescription = "Filter"
        )
    }

    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = dismissDialog,
            title = { Text(stringResource(R.string.select_activity_txt)) },
            text = {
                Column(horizontalAlignment = Alignment.Start) {
                    val allTypes =
                        Utils.RecordType.entries.filter { it != Utils.RecordType.AUTOMATIC }

                    allTypes.forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = selectedFilterTypes.contains(type),
                                onCheckedChange = {

                                    selectedFilterTypes = if (it) {
                                        selectedFilterTypes + type
                                    } else {
                                        selectedFilterTypes - type
                                    }
                                }
                            )
                            Text(type.name)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onFilterChange(selectedFilterTypes)
                        dismissDialog()
                    }
                ) {
                    Text(stringResource(R.string.apply_confirm_btn))
                }
            },
            dismissButton = {
                Button(onClick = dismissDialog) {
                    Text(stringResource(R.string.cancel_btn))
                }
            }
        )
    }
}


@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            )
        }
    }
}