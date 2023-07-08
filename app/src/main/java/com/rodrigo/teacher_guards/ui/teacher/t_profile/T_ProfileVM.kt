package com.rodrigo.teacher_guards.ui.teacher.t_profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.rodrigo.teacher_guards.data.dependencies.MyApplication
import com.rodrigo.teacher_guards.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class T_ProfileVM(
    private val application: Application,
    private val provideFirebaseAuth: GoogleSignInOptions,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<T_ProfileState> =
        MutableStateFlow(T_ProfileState())
    val uiState: StateFlow<T_ProfileState> = _uiState.asStateFlow()

    init {
        getData()
    }

    fun cerrarSesionGoogle() {
        val googleSignInClient = GoogleSignIn.getClient(application, provideFirebaseAuth)
        googleSignInClient.signOut().addOnCompleteListener { task ->

            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        logOutSuccess = task.isSuccessful,
                        logOutActionExecuting = true
                    )
                }
            }
        }
    }

    private fun getData() {
        viewModelScope.launch {
            userPreferencesRepository.getUserPrefs().collect { preferences ->
                _uiState.update {
                    it.copy(
                        email = preferences.email,
                        rol = preferences.rol,
                        getDataEnd = true
                    )
                }
            }
        }
    }

    fun dataLoaded() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    getDataEnd = false
                )
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication

                return T_ProfileVM(
                    application,
                    application.appcontainer.provideFirebaseAuth,
                    application.appcontainer.userPreferencesRepository
                ) as T
            }
        }
    }
}