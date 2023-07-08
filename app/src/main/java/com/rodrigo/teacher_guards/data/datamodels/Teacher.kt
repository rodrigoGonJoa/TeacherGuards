package com.rodrigo.teacher_guards.data.datamodels

data class Teacher(
    var id : String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val asistanceStatus: Boolean = false,
    val rol : String = "teacher"
) {
    fun setEmailId() {
        id = email.split("@")[0]
    }

    override fun toString(): String {
        return "Teacher(id='$id', name='$name', surname='$surname', email='$email', asistanceStatus=$asistanceStatus, rol='$rol')"
    }


}
