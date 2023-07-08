package com.rodrigo.teacher_guards.ui.administrator.a_add_teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.rodrigo.teacher_guards.data.dependencies.MyApplication
import com.rodrigo.teacher_guards.data.datamodels.Teacher
import com.rodrigo.teacher_guards.data.datamodels.TeacherGuardModel
import com.rodrigo.teacher_guards.data.datamodels.Structure.TeacherGetSchedule
import com.rodrigo.teacher_guards.data.repository.FirestoreAdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class A_AddTeacherVM(
    private val firestoreRepository: FirestoreAdminRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<A_AddTeacherState> =
        MutableStateFlow(A_AddTeacherState())
    val uiState: StateFlow<A_AddTeacherState> = _uiState.asStateFlow()

    fun addTeacher(teacher: Teacher, schedule: TeacherGetSchedule) {
        viewModelScope.launch {
            activateLoadCircle()

            firestoreRepository.addTeacher(
                teacher,
                schedule,
                TeacherGuardModel(0, teacher.id, teacher.name + " " + teacher.surname, 0),
                ::resultAdded
            )
        }
    }


    private fun resultAdded(success: Boolean) {
        _uiState.update {
            it.copy(
                teacherAddExecuting = true,
                teacherAdded = success
            )
        }
    }

    fun addTeacherExecuted() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    teacherAddExecuting = false,
                    teacherAdded = false,
                    loading = false
                )
            }
        }
    }

    private fun activateLoadCircle() {
        _uiState.update {
            it.copy(
                loading = true
            )
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

                return A_AddTeacherVM(
                    (application as MyApplication).appcontainer.firestoreAdminRepository
                ) as T
            }
        }
    }
}