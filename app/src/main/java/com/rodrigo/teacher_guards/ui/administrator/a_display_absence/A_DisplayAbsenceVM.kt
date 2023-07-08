package com.rodrigo.teacher_guards.ui.administrator.a_display_absence

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.TeacherGuardModel
import com.rodrigo.teacher_guards.data.datamodels.AbsentScheduleModel
import com.rodrigo.teacher_guards.data.datamodels.enums.CourseEnum
import com.rodrigo.teacher_guards.data.dependencies.MyApplication
import com.rodrigo.teacher_guards.data.repository.FirestoreAdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class A_DisplayAbsenceVM(
    private val _uiState: MutableStateFlow<A_DisplayAbsenceState>,
    private val firestoreAdminRepository: FirestoreAdminRepository
) : ViewModel() {
    val uiState: StateFlow<A_DisplayAbsenceState> = _uiState.asStateFlow()

    fun getAbsence(idAbsence: String) {
        viewModelScope.launch {
            firestoreAdminRepository.getAbsence(idAbsence) { absence ->

                val updatedDaysAbsent = absence.days_absent.map { dayAbsent ->
                    val updatedAbsentSchedule =
                        dayAbsent.absentSchedule.filterNot { absentSchedule ->
                            absentSchedule.course == CourseEnum.ERROR
                        }
                    dayAbsent.copy(absentSchedule = updatedAbsentSchedule)
                }
                val final = absence.copy(days_absent = updatedDaysAbsent)


                _uiState.update {
                    it.copy(
                        teacherAbsence = final,
                        loadStructure = true
                    )
                }
            }
        }
    }

    fun loadStructureExecuted() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadStructure = false
                )
            }
        }
    }

    fun loadDropList(idDay: String, context: Context) {
        viewModelScope.launch {
            firestoreAdminRepository.loadDropTeacherList(idDay) { list ->
                cacheDropList(list, context) { mutableMap ->
                    _uiState.update {
                        it.copy(
                            dropItemMap = mutableMap,
                            dropItemsLoaded = true,
                            dropItemsLoad = false
                        )
                    }
                }
            }
        }
    }

    private fun cacheDropList(
        teacherList: List<TeacherGuardModel>,
        context: Context,
        callback: (MutableMap<String, String>) -> Unit
    ) {
        val finalList = mutableMapOf<String, String>()
        for (teacher in teacherList) {

            val name = teacher.full_name
            val lblGuards = context.getString(R.string.a_set_teacher_drop_item_txt_guards)
            val guards = teacher.guard_amount
            val lblScore = context.getString(R.string.a_set_teacher_drop_item_txt_score)
            val score = teacher.score

            val fullText = "$name - $lblGuards $guards - $lblScore $score"
            finalList[teacher.id] = fullText
        }
        callback(finalList)
    }

    fun saveTeacher(
        idTeacherValue: String,
        dayAbsentData: AbsentScheduleModel,
        date: String,
        idDay: String
    ) {
        viewModelScope.launch {
            var idTeacherGuard = ""
            for (key in _uiState.value.dropItemMap.keys.toList()) {
                val value = _uiState.value.dropItemMap[key]
                if (idTeacherValue == value) {
                    idTeacherGuard = key
                    break
                }
            }
            val fullName = _uiState.value.dropItemMap[idTeacherGuard]?.split("-")?.get(0)!!
            firestoreAdminRepository.saveScore(
                idTeacherGuard,
                fullName,
                dayAbsentData,
                date,
                idDay
            ) { success ->
                _uiState.update {
                    it.copy(
                        successAddScore = success,
                        addScoreExecuting = true
                    )
                }
            }
        }
    }

    fun addScoreExecuted() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    addScoreExecuting = false,
                    dropItemsLoad = true,
                    dropItemsLoaded = false,
                    dropItemMap = mutableMapOf()
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

                return A_DisplayAbsenceVM(
                    application.appcontainer.adminStatesAbsence,
                    application.appcontainer.firestoreAdminRepository
                ) as T
            }
        }
    }
}