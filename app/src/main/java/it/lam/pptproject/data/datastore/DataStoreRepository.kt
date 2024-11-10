package it.lam.pptproject.data.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun putString(key: String, value: String)
    suspend fun putInt(key: String, value: Int)
    suspend fun getString(key: String): String?
    suspend fun getInt(key: String): Int?
    fun isTracking(): Flow<Boolean?>
    suspend fun setTracking(value: Boolean)
    suspend fun initializeDefaults()
    suspend fun setStartTime(value: Long)
    suspend fun getStartTime() : Long

}