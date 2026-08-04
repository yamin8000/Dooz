package io.github.yamin8000.dooz.common.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object DataStore {
    val Context.settings: DataStore<Preferences> by preferencesDataStore(name = "settings")
}