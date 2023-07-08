package com.rodrigo.teacher_guards.ui.teacher.t_display_absence

import com.rodrigo.teacher_guards.data.datamodels.TaskModel
import com.rodrigo.teacher_guards.data.datamodels.TeacherAbsenceModel

data class T_DisplayAbsenceState(
    val idUser : String = "",
    val absent_name: String = "",
    val absent_surname: String = "",
    val teacherAbsence : TeacherAbsenceModel? = null,
    val loadStructure : Boolean = false,
    val updateTaskAction : Boolean = false,
    val taskList : MutableList<TaskModel> = mutableListOf(),
    val cacheTask : TaskModel? = null,
    val saveAbsenceSuccess: Boolean = false,
    val saveAbsenceEnd: Boolean = false
)