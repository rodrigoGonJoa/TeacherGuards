package com.rodrigo.teacher_guards.ui.administrator.a_profile

data class A_ProfileState(
    val logOutSuccess: Boolean = false,
    val logOutActionExecuting : Boolean = false,
    val email: String = "",
    val rol : String = "",
    val getDataEnd : Boolean = false
)
