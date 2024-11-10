package it.lam.pptproject.repository

import it.lam.pptproject.data.room.MonthlySteps
import it.lam.pptproject.data.room.TypePercentageData
import it.lam.pptproject.model.room.StatisticsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ChartsRepository {
    fun getPercentage(): Flow<List<TypePercentageData>>
    fun getMonthlySteps(): Flow<List<MonthlySteps>>

}

class ChartsRepositoryImpl  @Inject constructor (private var statisticsDao: StatisticsDao) : ChartsRepository{

    override fun getPercentage(): Flow<List<TypePercentageData>> {
        return statisticsDao.getPercentuale()
    }

    override fun getMonthlySteps(): Flow<List<MonthlySteps>> {
        return statisticsDao.getMonthlySteps()
    }
}