package com.rodrigo.teacher_guards.ui.administrator.a_add_teacher

data class A_AddTeacherState(

    var loading : Boolean = false,
    var teacherAddExecuting: Boolean = false,

    var teacherAdded: Boolean = false
)