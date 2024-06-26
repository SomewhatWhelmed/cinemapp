package com.example.cinemapp.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.cinemapp.ui.main.model.AccountDetails
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlin.coroutines.coroutineContext

class UserPreferences (context: Context){

    private val Context.userPrefs: DataStore<Preferences> by preferencesDataStore(name = "preferences")
    private val userPrefs = context.userPrefs

    private fun getUserPrefs(): Flow<Preferences> {
        return userPrefs.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                Log.e("USER_PREF", exception.message ?: "Unknown error")
            }
        }
    }

    private val sessionIdKey = stringPreferencesKey("SESSION_ID_KEY")
    fun getSessionId(): Flow<String?> {
        return getUserPrefs().map { pref ->
            pref[sessionIdKey]
        }
    }
    suspend fun setSessionId(sessionId: String) {
        userPrefs.edit { pref ->
            pref[sessionIdKey] = sessionId
        }
    }
    suspend fun deleteSessionId() {
        userPrefs.edit { pref ->
            pref.remove(sessionIdKey)
        }
    }

}