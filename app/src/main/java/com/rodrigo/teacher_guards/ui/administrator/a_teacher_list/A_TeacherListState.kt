package com.rodrigo.teacher_guards.ui.administrator.a_teacher_list

import com.rodrigo.teacher_guards.data.datamodels.Teacher

data class A_TeacherListState(
    val teacherList : List<Teacher> = listOf(),
    val loading : Boolean = true
)