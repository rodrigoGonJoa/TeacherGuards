package com.rodrigo.teacher_guards.ui.administrator.a_absence_list

import com.rodrigo.teacher_guards.data.datamodels.AdminAbsenceListModel

data class A_AbsenceListState(
    val absenceList: List<AdminAbsenceListModel> = listOf(),
    val loading: Boolean = true,
    val absenceListUpdated: Boolean = false
)