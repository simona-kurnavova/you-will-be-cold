package com.youllbecold.trustme.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private const val USER_PREFERENCES_NAME = "user_preferences"
private const val PREFERENCES_DAILY_NOTIF = "allow_daily_notif"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

/**
 * Object for accessing data store preferences.
 */
class DataStorePreferences(private val context: Context) {

    private val dailyNotificationKey = booleanPreferencesKey(PREFERENCES_DAILY_NOTIF)

    /**
     * Flow denoting whether daily notification is enabled.
     */
    val allowDailyNotification: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            Log.d("AppDataStorePreferences", "Error reading preferences", exception)
            flow { emit(false) }
        }.map { preferences ->
            preferences[dailyNotificationKey] ?: false
        }

    /**
     * Set whether daily notification is enabled.
     *
     * @param allow True if daily notification is enabled, false otherwise.
     */
    suspend fun setAllowDailyNotification(allow: Boolean) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[dailyNotificationKey] = allow
            }
        }
    }
}
