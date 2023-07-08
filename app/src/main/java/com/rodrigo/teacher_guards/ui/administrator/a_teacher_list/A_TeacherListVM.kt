package com.rodrigo.teacher_guards.ui.administrator.a_teacher_list


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

class A_TeacherListVM(
    private val firestoreAdminRepository: FirestoreAdminRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<A_TeacherListState> =
        MutableStateFlow(A_TeacherListState())
    val uiState: StateFlow<A_TeacherListState> = _uiState.asStateFlow()

    init {
        getTeachers()
    }

    fun getTeachers() {
        firestoreAdminRepository.getTeachers { teacherList ->
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        teacherList = teacherList,
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

                return A_TeacherListVM(
                    (application as MyApplication).appcontainer.firestoreAdminRepository
                ) as T
            }
        }
    }
}