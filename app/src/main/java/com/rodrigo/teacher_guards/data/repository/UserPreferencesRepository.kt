package com.rodrigo.teacher_guards.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.rodrigo.teacher_guards.data.datamodels.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val userDataStore: DataStore<Preferences>
) {
    suspend fun saveData(name: String, email: String, rol : String){
        userDataStore.edit { userPreferences ->
            userPreferences[stringPreferencesKey(UserPreferences.NAME)] = name
            userPreferences[stringPreferencesKey(UserPreferences.EMAIL)] = email
            userPreferences[stringPreferencesKey(UserPreferences.ROL)] = rol
        }
    }

    fun getUserPrefs(): Flow<UserPreferences> {
        return userDataStore.data.map { userPreferences ->
            val name = userPreferences[stringPreferencesKey(UserPreferences.NAME)] ?: ""
            val email = userPreferences[stringPreferencesKey(UserPreferences.EMAIL)] ?: ""
            val rol = userPreferences[stringPreferencesKey(UserPreferences.ROL)] ?: ""
            return@map UserPreferences(
                name = name,
                email = email,
                rol = rol
            )
        }
    }

    companion object{
        fun getUserId(email: String): String{
            return email.split("@")[0]
        }
    }
}