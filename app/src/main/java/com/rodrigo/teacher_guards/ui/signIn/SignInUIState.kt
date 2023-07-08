package com.rodrigo.teacher_guards.ui.signIn

data class SignInUIState(
    val isLoading : Boolean = false,
    val loginSuccess : Boolean = false,
    val userRol : UserRol = UserRol.ERROR,
    val loginActionExecuting : Boolean = false,
    val email : String = "",
    val checkingEmail : Boolean = false
)
