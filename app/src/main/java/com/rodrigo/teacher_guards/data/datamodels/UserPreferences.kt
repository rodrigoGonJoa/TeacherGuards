package com.rodrigo.teacher_guards.data.datamodels

data class UserPreferences(
    val name: String = "",
    val email: String = "",
    val rol : String = RolError
) {
    companion object {
        const val SETTINGS_FILE = "settings"
        const val NAME = "name"
        const val EMAIL = "email"
        const val ROL = "rol"
        const val RolError = "error"
    }
}
