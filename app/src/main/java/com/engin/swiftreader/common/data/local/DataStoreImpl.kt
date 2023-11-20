package com.engin.swiftreader.common.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.engin.swiftreader.common.domain.PersistenceKeyValueRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PersistenceKeyValueRepository {

    override suspend fun getString(key: String) :String? {
        val prefKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[prefKey]
    }

    override suspend fun getInt(key: String): Int? {
        val prefKey = intPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[prefKey]
    }

    override suspend fun getBoolean(key: String): Boolean? {
        val prefKey = booleanPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[prefKey]
    }

    override suspend fun putString(key: String,value :String) {
        val prefKey = stringPreferencesKey(key)
        dataStore.edit {
            it[prefKey] = value
        }
    }

    override suspend fun putInt(key: String, value: Int) {
        val prefKey = intPreferencesKey(key)
        dataStore.edit {
            it[prefKey] = value
        }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        val prefKey = booleanPreferencesKey(key)
        dataStore.edit {
            it[prefKey] = value
        }
    }

    override suspend fun removeValue(key: String) {
        val prefKey = stringPreferencesKey(key)
        dataStore.edit {
            if (it.contains(prefKey))
                it.remove(prefKey)
        }
    }
}