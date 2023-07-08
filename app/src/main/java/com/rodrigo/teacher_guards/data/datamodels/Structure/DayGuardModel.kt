package com.rodrigo.teacher_guards.data.datamodels.Structure

import com.rodrigo.teacher_guards.data.datamodels.TeacherGuardModel

data class DayGuardModel(
    val day: String = "",
    val hour: Int = 0,
    val users: List<TeacherGuardModel>? = null
)