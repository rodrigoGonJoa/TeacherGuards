package com.rodrigo.teacher_guards.ui.administrator.a_display_teacher

import com.rodrigo.teacher_guards.data.datamodels.Teacher

data class A_DisplayTeacherState(
    val loading : Boolean = true,
    val teacherId : String = "",
    val teacher : Teacher? = null
)