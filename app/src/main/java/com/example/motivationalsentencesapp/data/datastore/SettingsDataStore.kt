package com.example.motivationalsentencesapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    private object PreferencesKeys {
        val TEXT_COLOR = intPreferencesKey("text_color")
    }

    val textColorFlow: Flow<Int> = context.dataStore.data
        .map {
            it[PreferencesKeys.TEXT_COLOR] ?: android.graphics.Color.WHITE
        }

    suspend fun saveTextColor(color: Int) {
        context.dataStore.edit {
            it[PreferencesKeys.TEXT_COLOR] = color
        }
    }
}
