package com.rodrigo.teacher_guards.ui.teacher.t_display_guard

import com.rodrigo.teacher_guards.data.datamodels.TaskModel

data class T_DisplayGuardState(
    val guard: TaskModel = TaskModel(),
    val guardSaved: Boolean = false
)