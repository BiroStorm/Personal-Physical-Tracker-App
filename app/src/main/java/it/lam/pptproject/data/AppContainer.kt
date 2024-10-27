// FROM ANDROID PROJECT
// https://developer.android.com/codelabs/basic-android-kotlin-compose-persisting-data-room
package it.lam.pptproject.data

import android.content.Context
import it.lam.pptproject.model.room.AppDatabase
import it.lam.pptproject.repository.ChartsRepository
import it.lam.pptproject.repository.TrackingRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val trackingRepository: TrackingRepository
}

/**
 * [AppContainer] implementation that provides instance of [chartsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [chartsRepository]
     */
    override val trackingRepository: TrackingRepository by lazy {
        TrackingRepository(AppDatabase.getDatabase(context).trackingDataDao(), context)
    }
}
