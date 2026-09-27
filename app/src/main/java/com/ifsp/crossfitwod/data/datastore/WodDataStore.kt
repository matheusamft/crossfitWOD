package com.ifsp.crossfitwod.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ifsp.crossfitwod.data.model.Wod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wods_prefs")

class WodDataStore(private val context: Context) {

    private val WODS_KEY = stringPreferencesKey("wods_list")

    val wodsFlow: Flow<List<Wod>> = context.dataStore.data
        .map { preferences ->
            val wodsJson = preferences[WODS_KEY] ?: "[]"
            try {
                Json.decodeFromString<List<Wod>>(wodsJson)
            } catch (e: Exception) {
                emptyList()
            }
        }

    suspend fun saveWods(wods: List<Wod>) {
        context.dataStore.edit { preferences ->
            preferences[WODS_KEY] = Json.encodeToString(wods)
        }
    }
}
