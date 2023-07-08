package com.rodrigo.teacher_guards.ui.administrator.a_display_absence

import com.rodrigo.teacher_guards.data.datamodels.TaskModel
import com.rodrigo.teacher_guards.data.datamodels.TeacherAbsenceModel

data class A_DisplayAbsenceState(
    val teacherAbsence: TeacherAbsenceModel? = null,
    val loadStructure: Boolean = false,
    val updateTaskAction: Boolean = false,
    val taskList: MutableList<TaskModel> = mutableListOf(),
    val cacheTask: TaskModel? = null,
    val saveAbsenceSuccess: Boolean = false,
    val saveAbsenceEnd: Boolean = false,

    val dropItemsLoaded: Boolean = false,
    val dropItemMap: MutableMap<String, String> = mutableMapOf(),
    val dropItemsLoad: Boolean = true,

    val successAddScore: Boolean = false,
    val addScoreExecuting: Boolean = false

)