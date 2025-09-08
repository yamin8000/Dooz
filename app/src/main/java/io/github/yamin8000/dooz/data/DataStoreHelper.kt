package io.github.yamin8000.dooz.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class DataStoreHelper(
    private val datastore: DataStore<Preferences>
) {

    suspend fun getBoolean(
        key: String
    ) = datastore.data.map {
        it[booleanPreferencesKey(key)]
    }.firstOrNull()

    suspend fun setBoolean(
        key: String,
        value: Boolean
    ) {
        datastore.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun getString(
        key: String
    ) = datastore.data.map {
        it[stringPreferencesKey(key)]
    }.firstOrNull()

    suspend fun setString(
        key: String,
        value: String
    ) {
        datastore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    suspend fun getInt(
        key: String
    ) = datastore.data.map {
        it[intPreferencesKey(key)]
    }.firstOrNull()

    suspend fun setInt(
        key: String,
        value: Int
    ) {
        datastore.edit {
            it[intPreferencesKey(key)] = value
        }
    }
}