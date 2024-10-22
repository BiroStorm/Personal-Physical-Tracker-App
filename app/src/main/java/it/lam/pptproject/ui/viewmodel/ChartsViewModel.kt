package it.lam.pptproject.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import it.lam.pptproject.data.room.TypePercentageData
import it.lam.pptproject.repository.ChartsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChartsViewModel(private val repository: ChartsRepository) : ViewModel() {

    private val _chartEntryModel = MutableStateFlow(entryModelOf(
        entriesOf(4f, 12f, 8f, 16f),
        entriesOf(12f, 16f, 4f, 12f)
    ))


    val chartEntryModel: StateFlow<ChartEntryModel> = _chartEntryModel.asStateFlow()

    // * .asStateFlow() è un metodo di Kotlin che permette di trasformare un MutableStateFlow in un StateFlow.






}


/*
class ChartsViewModelFactory(private val repository: ChartsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChartsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChartsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

*/
