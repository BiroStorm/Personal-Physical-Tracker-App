package it.lam.pptproject.repository

import it.lam.pptproject.data.room.TrackingDataWithDate
import it.lam.pptproject.model.room.ActivityDao
import it.lam.pptproject.utils.Utils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ActivityRepository {
    fun getActivities(value: List<Utils.RecordType>): Flow<List<TrackingDataWithDate>>


}

class ActivityRepositoryImpl @Inject constructor(
    private var activityDao: ActivityDao,
) : ActivityRepository {
    override fun getActivities(value: List<Utils.RecordType>): Flow<List<TrackingDataWithDate>> {
        return activityDao.getActivities(value)
    }


}