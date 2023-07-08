package com.rodrigo.teacher_guards.ui.administrator.a_display_teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.rodrigo.teacher_guards.data.dependencies.MyApplication
import com.rodrigo.teacher_guards.data.repository.FirestoreAdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class A_DisplayTeachertVM(
    private val firestoreAdminRepository: FirestoreAdminRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<A_DisplayTeacherState> =
        MutableStateFlow(A_DisplayTeacherState())
    val uiState: StateFlow<A_DisplayTeacherState> = _uiState.asStateFlow()

    fun loadViewData(idTeacher: String) {
        firestoreAdminRepository.getTeacher(idTeacher) { teacher ->
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        teacher = teacher,
                        loading = false
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
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return A_DisplayTeachertVM(
                    (application as MyApplication).appcontainer.firestoreAdminRepository
                ) as T
            }
        }
    }
}