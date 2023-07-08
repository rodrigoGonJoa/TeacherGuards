package com.rodrigo.teacher_guards.data.datamodels

import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum

data class TeacherAbsenceListModel(
    val dateFrom : String = "",
    val dateTo : String = "",
    val weekDay : WeekDayEnum = WeekDayEnum.ERROR,
    var absence_ref : String = ""
){
    fun setAbsenceRef(id_user : String){
        absence_ref = dateFrom +"_"+ id_user
    }
}