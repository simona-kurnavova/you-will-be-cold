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
private const val PREFERENCES_RECOMMEND_NOTIF = "allow_recom_notif"
private const val PREFERENCES_USE_CELSIUS_UNITS = "use_celsius_units"
private const val PREFERENCES_WELCOME_SCREEN_SHOWN = "welcome_screen_shown"


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

/**
 * Object for accessing data store preferences.
 */
class DataStorePreferences(private val context: Context) {

    private val dailyNotificationKey = booleanPreferencesKey(PREFERENCES_DAILY_NOTIF)
    private val recommendNotificationKey = booleanPreferencesKey(PREFERENCES_RECOMMEND_NOTIF)
    private val useCelsiusUnitsKey = booleanPreferencesKey(PREFERENCES_USE_CELSIUS_UNITS)
    private val welcomeScreenShownKey = booleanPreferencesKey(PREFERENCES_WELCOME_SCREEN_SHOWN)

    /**
     * Flow denoting whether daily notification is enabled.
     */
    val allowDailyNotification: Flow<Boolean> = get(dailyNotificationKey, false)

    /**
     * Set whether daily notification is enabled.
     *
     * @param allow True if daily notification is enabled, false otherwise.
     */
    suspend fun setAllowDailyNotification(allow: Boolean) {
        edit(dailyNotificationKey, allow)
    }

    /**
     * Flow denoting whether recommendation notification is enabled.
     */
    val allowRecommendNotification: Flow<Boolean> = get(recommendNotificationKey, false)

    /**
     * Set whether recommendation notification is enabled.
     *
     * @param allow True if notification is enabled, false otherwise.
     */
    suspend fun setAllowRecommendNotification(allow: Boolean) {
        edit(recommendNotificationKey, allow)
    }

    /**
     * Flow denoting whether to use Celsius units. True by default.
     */
    val useCelsiusUnits: Flow<Boolean> = get(useCelsiusUnitsKey, true)

    /**
     * Set whether to use Celsius units.
     */
    suspend fun setUseCelsiusUnits(useCelsius: Boolean) {
        edit(useCelsiusUnitsKey, useCelsius)
    }

    /**
     * Flow denoting whether the welcome screen has been shown.
     */
    val welcomeScreenShown: Flow<Boolean> = get(welcomeScreenShownKey, false)

    /**
     * Set whether the welcome screen has been shown.
     */
    suspend fun setWelcomeScreenShown(shown: Boolean) {
        edit(welcomeScreenShownKey, shown)
    }

    private fun <T> get(key: Preferences. Key<T>, default: T): Flow<T> =
        context.dataStore.data.catch { exception ->
            Log.d("AppDataStorePreferences", "Error reading preferences", exception)
            flow { emit(default) }
        }.map { preferences ->
            preferences[key] ?: default
        }

    private suspend fun <T> edit(key: Preferences.Key<T>, newValue: T) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[key] = newValue
            }
        }
    }
}
