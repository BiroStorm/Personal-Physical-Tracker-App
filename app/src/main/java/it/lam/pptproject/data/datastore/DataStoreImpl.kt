package it.lam.pptproject.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import it.lam.pptproject.utils.AppStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// REFERENCE: https://medium.com/@vgoyal_1/datastore-android-how-to-use-it-like-a-pro-using-kotlin-2c2440683d78

private const val PREFERENCES_NAME = "project_ppt_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreImpl @Inject constructor(
    private val context: Context,
) : DataStoreRepository {

    override suspend fun initializeDefaults() {
        val username : Boolean? = isTracking().first()
        if(username == null){
            setTracking(false)
        }
    }

    override suspend fun putString(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun putInt(key: String, value: Int) {
        val preferencesKey = intPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getString(key: String): String? {
        return try {
            val preferencesKey = stringPreferencesKey(key)
            // * dataStore.data Ritorna un Flow<T>
            val preferences = context.dataStore.data.first()
            preferences[preferencesKey]
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    override suspend fun getInt(key: String): Int? {
        return try {
            val preferencesKey = intPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            preferences[preferencesKey]
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    override fun isTracking(): Flow<Boolean?> {
            val preferencesKey = booleanPreferencesKey("isTracking")
            return context.dataStore.data
                .map { preferences ->
                    preferences[preferencesKey]
                }

    }

    override suspend fun setTracking(value: Boolean) {
        val preferencesKey = booleanPreferencesKey("isTracking")
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }


}