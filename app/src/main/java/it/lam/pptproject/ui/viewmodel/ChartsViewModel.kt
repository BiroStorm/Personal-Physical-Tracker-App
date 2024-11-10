package it.lam.pptproject.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import it.lam.pptproject.data.room.MonthlySteps
import it.lam.pptproject.data.room.TypePercentageData
import it.lam.pptproject.repository.ChartsRepository
import javax.inject.Inject

@HiltViewModel
class ChartsViewModel @Inject constructor(private val repository: ChartsRepository) : ViewModel() {

    // * LiveData è read-only, mentre MutableLiveData è read-write.
    val percentage: LiveData<List<TypePercentageData>> = repository.getPercentage().asLiveData()


    val monthlySteps: LiveData<List<MonthlySteps>> = repository.getMonthlySteps().asLiveData()

}


