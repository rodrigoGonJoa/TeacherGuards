package com.rodrigo.teacher_guards.data.datamodels

import java.io.Serializable

data class TeacherGuardModel(
    var guard_amount : Int = 0,
    val id: String = "",
    val full_name: String = "",
    var score: Int = 0
)  : Serializable
