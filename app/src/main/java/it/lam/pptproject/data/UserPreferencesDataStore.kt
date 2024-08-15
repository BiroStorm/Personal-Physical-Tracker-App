package it.lam.pptproject.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_preferences")

object UserPreferencesDataStore {
    private val USER_NAME_KEY = stringPreferencesKey("user_name")

    fun getUserName(context: Context): Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[USER_NAME_KEY]
            }
    }

    suspend fun saveUserName(context: Context, userName: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = userName
        }
    }
}