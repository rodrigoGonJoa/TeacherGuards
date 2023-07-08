package com.rodrigo.teacher_guards.ui.teacher.t_guard_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.rodrigo.teacher_guards.data.datamodels.TeacherCoveredGuardsModel
import com.rodrigo.teacher_guards.data.dependencies.MyApplication
import com.rodrigo.teacher_guards.data.repository.FirestoreTeacherRepository
import com.rodrigo.teacher_guards.data.repository.UserPreferencesRepository
import com.rodrigo.teacher_guards.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class T_GuardListVM(
    private val firestoreTeacherRepository: FirestoreTeacherRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val _uiState: MutableStateFlow<T_GuardListState> =
        MutableStateFlow(T_GuardListState())
    val uiState: StateFlow<T_GuardListState> = _uiState.asStateFlow()

    fun displayInitiator() {
        viewModelScope.launch {
            userPreferencesRepository.getUserPrefs().collect { preferences ->
                _uiState.update {
                    it.copy(
                        idUser = UserPreferencesRepository.getUserId(preferences.email),
                        getIdUserEnd = true
                    )
                }
            }
        }
    }


    fun getGuards() {
        firestoreTeacherRepository.getTeacherGuards(_uiState.value.idUser) { list ->
            val coveredGuardList: MutableList<TeacherCoveredGuardsModel> = mutableListOf()
            val dailyGuardList: MutableList<TeacherCoveredGuardsModel> = mutableListOf()
            for (guard in list) {
                if (guard.date == Utils.getToday()) {
                    dailyGuardList.add(guard)
                } else if (guard.date < Utils.getToday()) {
                    coveredGuardList.add(guard)
                }
            }
            _uiState.update {
                it.copy(
                    coveredList = coveredGuardList,
                    dailyList = dailyGuardList,
                    getGuardListExecuting = true
                )
            }
        }
    }

    fun getGuardListExecuted() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    getGuardListExecuting = false,
                    getIdUserEnd = false
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

                return T_GuardListVM(
                    application.appcontainer.firestoreTeacherRepository,
                    application.appcontainer.userPreferencesRepository
                ) as T
            }
        }
    }
}