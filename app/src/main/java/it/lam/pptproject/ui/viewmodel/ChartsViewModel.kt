package it.lam.pptproject.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import dagger.hilt.android.lifecycle.HiltViewModel
import it.lam.pptproject.data.room.TypePercentageData
import it.lam.pptproject.repository.ChartsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChartsViewModel @Inject constructor (private val repository: ChartsRepository) : ViewModel() {

    // * LiveData è read-only, mentre MutableLiveData è read-write.
    val percentage :LiveData<List<TypePercentageData>> = repository.getPercentage().asLiveData()

}


