package com.rodrigo.teacher_guards.ui.teacher.t_display_guard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.viewModelFactory
import com.rodrigo.teacher_guards.data.dependencies.MyApplication
import com.rodrigo.teacher_guards.data.repository.FirestoreTeacherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class T_DisplayGuardVM(
    private val firestoreTeacherRepository: FirestoreTeacherRepository
) : ViewModel() {
    val _uiState: MutableStateFlow<T_DisplayGuardState> =
        MutableStateFlow(T_DisplayGuardState())
    val uiState: StateFlow<T_DisplayGuardState> = _uiState.asStateFlow()

    fun getGuard(idGuard: String, date: String) {
        viewModelScope.launch {
            firestoreTeacherRepository.getGuard(date, idGuard) { task ->
                _uiState.update {
                    it.copy(
                        guard = task,
                        guardSaved = true
                    )
                }
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

                return T_DisplayGuardVM(
                    application.appcontainer.firestoreTeacherRepository
                ) as T
            }
        }
    }
}