package it.lam.pptproject.repository

import android.util.Log
import androidx.lifecycle.asLiveData
import it.lam.pptproject.data.room.TypePercentageData
import it.lam.pptproject.model.room.StatisticsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

class ChartsRepository(private var statisticsDao: StatisticsDao) {



    fun getPercentage(): Flow<List<TypePercentageData>>{
        return statisticsDao.getPercentuale()
    }
}