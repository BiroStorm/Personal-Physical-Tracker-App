package it.lam.pptproject.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("application_pref")

object ButtonStateDataStore {
    private val APPLICATION_LISTENING = booleanPreferencesKey("application_listening")

    fun getApplicationState(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[APPLICATION_LISTENING] ?: false
            }
    }

    suspend fun saveApplicationState(context: Context, isEnd: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[APPLICATION_LISTENING] = isEnd
        }
    }
}