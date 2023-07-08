package com.rodrigo.teacher_guards.ui.administrator.a_absence_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.rodrigo.teacher_guards.data.dependencies.MyApplication
import com.rodrigo.teacher_guards.data.repository.FirestoreAdminRepository
import com.rodrigo.teacher_guards.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class A_AbsenceListVM(
    private val firestoreRepository: FirestoreAdminRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<A_AbsenceListState> =
        MutableStateFlow(A_AbsenceListState())
    val uiState: StateFlow<A_AbsenceListState> = _uiState.asStateFlow()

    init {
        initRecyclerView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initRecyclerView() {
        viewModelScope.launch {
            firestoreRepository.getAbsenceList(Utils.getToday()) { list ->
                _uiState.update {
                    it.copy(
                        absenceList = list,
                        loading = false,
                        absenceListUpdated = true
                    )
                }
            }
        }
    }

    fun getAbsenceList(date: String) {
        viewModelScope.launch {
            firestoreRepository.getAbsenceList(date) { list ->
                _uiState.update {
                    it.copy(
                        absenceList = list,
                        loading = false,
                        absenceListUpdated = true
                    )
                }
            }
        }
    }

    fun resetValues() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loading = false,
                    absenceListUpdated = true
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
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return A_AbsenceListVM(
                    (application as MyApplication).appcontainer.firestoreAdminRepository
                ) as T
            }
        }
    }
}