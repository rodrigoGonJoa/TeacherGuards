package com.rodrigo.teacher_guards.ui.teacher.t_absence_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.rodrigo.teacher_guards.data.dependencies.MyApplication
import com.rodrigo.teacher_guards.data.repository.FirestoreTeacherRepository
import com.rodrigo.teacher_guards.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class T_AbsenceListVM(
    private val firestoreTeacherRepository: FirestoreTeacherRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val _uiState: MutableStateFlow<T_AbsenceListState> =
        MutableStateFlow(T_AbsenceListState())
    val uiState: StateFlow<T_AbsenceListState> = _uiState.asStateFlow()


    init {
        loadUserPreferences()
    }

    private fun loadUserPreferences() {
        viewModelScope.launch {
            userPreferencesRepository.getUserPrefs().collect { preferences ->
                _uiState.update {
                    it.copy(
                        idUser = UserPreferencesRepository.getUserId(preferences.email),
                        loadPreferences = true
                    )
                }
            }
        }
    }

    fun loadAbsenceList() {
        firestoreTeacherRepository.getTeacherAbsenceList(_uiState.value.idUser) { absenceList ->
            _uiState.update {
                it.copy(
                    absenceList = absenceList,
                    loadListEx = true,
                    loadPreferences = false
                )
            }
        }
    }

    fun absenceListLoaded() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadListEx = false
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
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as MyApplication

                return T_AbsenceListVM(
                    application.appcontainer.firestoreTeacherRepository,
                    application.appcontainer.userPreferencesRepository
                ) as T
            }
        }
    }
}



