package it.lam.pptproject.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.lam.pptproject.data.room.TrackingDataWithDate
import it.lam.pptproject.repository.ActivityRepository
import it.lam.pptproject.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: ActivityRepository,
) : ViewModel() {

    private val _selectedActivityTypes = MutableStateFlow(
        listOf(Utils.RecordType.WALKING, Utils.RecordType.DRIVING, Utils.RecordType.SITTING)
    )
    val selectedActivityTypes = _selectedActivityTypes.asStateFlow()

    private val _activitiesByDate =
        MutableStateFlow<Map<LocalDate, List<TrackingDataWithDate>>>(emptyMap())
    val activitiesByDate: StateFlow<Map<LocalDate, List<TrackingDataWithDate>>> = _activitiesByDate

    fun setActivityFilter(types: List<Utils.RecordType>) {
        Log.d("CalendarViewModel", "setActivityFilter: $types")
        _selectedActivityTypes.value = types
        loadActivities()
    }

    init {
        loadActivities()
    }

    private fun loadActivities() {
        Log.d("CalendarViewModel", "loadActivities")
        val types = _selectedActivityTypes.value
        viewModelScope.launch {
            repository.getActivities(types)
                .collect { activities ->
                    // Raggruppa le attività per data
                    val groupedActivities = activities.groupBy {
                        Instant.ofEpochMilli(it.startTime.time).atZone(
                            ZoneId.systemDefault()
                        ).toLocalDate()
                    }
                    _activitiesByDate.value = groupedActivities
                }
        }
    }

}