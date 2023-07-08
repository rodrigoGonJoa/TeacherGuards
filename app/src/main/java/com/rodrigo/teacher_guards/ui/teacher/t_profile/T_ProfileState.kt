package com.rodrigo.teacher_guards.ui.teacher.t_profile

data class T_ProfileState(
    val logOutSuccess: Boolean = false,
    val logOutActionExecuting : Boolean = false,
    val email: String = "",
    val rol : String = "",
    val getDataEnd : Boolean = false
)