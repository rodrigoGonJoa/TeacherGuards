package com.rodrigo.teacher_guards.data.datamodels

data class AdminAbsenceListModel(
    val date_from: String = "",
    val date_to: String = "",
    val absent_teacher_id: String = "",
    val absence_id : String = "",
    val teacher_name: String = "",
    val teacher_surname: String = "",
)