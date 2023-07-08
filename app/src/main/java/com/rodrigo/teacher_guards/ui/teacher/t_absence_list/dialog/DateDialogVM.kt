package com.rodrigo.teacher_guards.ui.teacher.t_absence_list.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.rodrigo.teacher_guards.ui.teacher.t_absence_list.T_AbsenceListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DateDialogVM(private var _uiState: MutableStateFlow<T_AbsenceListState>) : ViewModel() {

    val uiState: StateFlow<T_AbsenceListState> = _uiState.asStateFlow()

    fun saveDates(dateFrom: String, dateTo: String){
        var action = true
        if(dateFrom == "" && dateTo == ""){
            action = false
        }
        viewModelScope.launch {
            _uiState.update{
                it.copy(
                    actionSaveDates = action,
                    dateFrom = dateFrom,
                    dateTo = dateTo
                )
            }
        }
    }


    companion object {
        @Suppress("UNCHECKED_CAST")
        class DateDialogVMFactory(private val uiState: MutableStateFlow<T_AbsenceListState>) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                if (modelClass.isAssignableFrom(DateDialogVM::class.java)) {
                    return DateDialogVM(uiState) as T
                }
                throw IllegalArgumentException()
            }
        }
    }

}
