package com.rodrigo.teacher_guards.ui.teacher.t_display_absence

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.rodrigo.teacher_guards.data.dependencies.MyApplication
import com.rodrigo.teacher_guards.data.repository.FirestoreTeacherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.rodrigo.teacher_guards.data.datamodels.TaskModel
import com.rodrigo.teacher_guards.data.datamodels.TeacherAbsenceModel
import com.rodrigo.teacher_guards.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class T_DisplayAbsenceVM(
    private val _uiState: MutableStateFlow<T_DisplayAbsenceState>,
    private val firestoreTeacherRepository: FirestoreTeacherRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val uiState: StateFlow<T_DisplayAbsenceState> = _uiState.asStateFlow()


    fun displayInitiator() {
        viewModelScope.launch {
            userPreferencesRepository.getUserPrefs().collect { preferences ->
                _uiState.update {
                    it.copy(
                        idUser = UserPreferencesRepository.getUserId(preferences.email)
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadStructure(dateFrom: String, dateTo: String, context: Context) {
        viewModelScope.launch {
            firestoreTeacherRepository.createTasks(
                _uiState.value.idUser,
                dateFrom,
                dateTo,
                context
            ) { teacherAbsence ->
                loadTaskList(teacherAbsence) { taskList ->
                    _uiState.update {
                        it.copy(
                            taskList = taskList,
                            teacherAbsence = teacherAbsence,
                            loadStructure = true
                        )
                    }
                }
            }
        }
    }

    fun addTask(task: TaskModel) {
        val absentName = _uiState.value.absent_name
        val absentSurname = _uiState.value.absent_surname

        viewModelScope.launch {
            if (absentName == "" && absentSurname == "") {
                firestoreTeacherRepository.getTeacherData(_uiState.value.idUser) {
                    val name = it.name
                    val surname = it.surname
                    task.absentTeacherName = name
                    task.absentTeacherSurname = surname

                    _uiState.update { state ->
                        state.copy(
                            taskList = state.taskList.plus(task).toMutableList(),
                            updateTaskAction = true,
                            absent_name = name,
                            absent_surname = surname
                        )
                    }
                }
            } else {
                _uiState.update { state ->
                    task.absentTeacherName = absentName
                    task.absentTeacherSurname = absentSurname
                    state.copy(
                        taskList = state.taskList.plus(task).toMutableList(),
                        updateTaskAction = true
                    )
                }
            }
        }
    }

    fun cacheTask(task: TaskModel){
        _uiState.update {
            it.copy(
                cacheTask = task,
                updateTaskAction = true
            )
        }
    }

    fun updateTask(task: TaskModel) {
        viewModelScope.launch {
            val taskList = _uiState.value.taskList.toMutableList()

            val taskToInsert = taskList.find {
                it.hour == task.hour && it.course == task.course
            }
            taskToInsert?.description = task.description

            val index = taskList.indexOf(taskToInsert)

            if (taskToInsert != null) {
                taskList.removeAt(index)
                taskList.add(index, taskToInsert)
            }

            _uiState.update {
                it.copy(
                    taskList = taskList
                )
            }
        }
    }


    private fun loadTaskList(
        teacherAbsenceModel: TeacherAbsenceModel,
        callback: (MutableList<TaskModel>) -> Unit
    ) {
        val taskList = mutableListOf<TaskModel>()
        for (dayAbsent in teacherAbsenceModel.days_absent) {
            for (absentSchedule in dayAbsent.absentSchedule) {
                val task = TaskModel(
                    date = dayAbsent.date,
                    weekDay = dayAbsent.weekDay,
                    hour = absentSchedule.hour,
                    absentTeacherName = "",
                    absentTeacherSurname = "",
                    course = absentSchedule.course,
                    subject = absentSchedule.subject,
                    description = "",
                )
                taskList.add(task)
            }
        }
        addTaskNameSurname(taskList) {
            callback(it)
        }
    }

    private fun addTaskNameSurname(
        taskList: List<TaskModel>,
        callback: (MutableList<TaskModel>) -> Unit
    ) {
        var absentName = _uiState.value.absent_name
        var absentSurname = _uiState.value.absent_surname
        val finalTaskList = mutableListOf<TaskModel>()

        if (absentName == "" || absentSurname == "") {
            firestoreTeacherRepository.getTeacherData(_uiState.value.idUser) { teacher ->
                absentName = teacher.name
                absentSurname = teacher.surname

                for (task in taskList) {
                    task.absentTeacherName = absentName
                    task.absentTeacherSurname = absentSurname
                    finalTaskList.add(task)
                }
                callback(finalTaskList)
            }
        } else {
            for (task in taskList) {
                task.absentTeacherName = absentName
                task.absentTeacherSurname = absentSurname
                finalTaskList.add(task)
            }
            callback(finalTaskList)
        }
    }


    fun endAddTaskAction() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    updateTaskAction = false
                )
            }
        }
    }

    fun saveAbsence() {
        viewModelScope.launch{
            firestoreTeacherRepository.addAbsence(
                _uiState.value.teacherAbsence,
                _uiState.value.taskList,
                _uiState.value.idUser
            ){
                _uiState.update { state ->
                    state.copy(
                        saveAbsenceEnd = true,
                        saveAbsenceSuccess = it
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

                return T_DisplayAbsenceVM(
                    application.appcontainer.teacherStateAbsence,
                    application.appcontainer.firestoreTeacherRepository,
                    application.appcontainer.userPreferencesRepository
                ) as T
            }
        }
    }
}