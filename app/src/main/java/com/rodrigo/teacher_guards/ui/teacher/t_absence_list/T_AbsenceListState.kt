package com.rodrigo.teacher_guards.ui.teacher.t_absence_list

import com.rodrigo.teacher_guards.data.datamodels.TeacherAbsenceListModel

data class T_AbsenceListState (
    val dateFrom : String = "",
    val dateTo : String = "",
    val actionSaveDates : Boolean = false,
    val loading : Boolean = false,
    val idUser : String = "",
    val absenceList : List<TeacherAbsenceListModel> = listOf(),
    val loadListEx : Boolean = false,
    val loadPreferences: Boolean = false
)