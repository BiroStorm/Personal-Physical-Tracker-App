package it.lam.pptproject.repository

import it.lam.pptproject.data.room.TypePercentageData
import it.lam.pptproject.model.room.StatisticsDao

class ChartsRepository(private var statisticsDao: StatisticsDao) {


    suspend fun getPercentuale(username: String): List<TypePercentageData?> {
        return statisticsDao.getPercentuale(username)
    }
}