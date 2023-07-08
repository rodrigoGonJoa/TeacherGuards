package com.rodrigo.teacher_guards.ui.teacher.t_guard_list

import com.rodrigo.teacher_guards.data.datamodels.TeacherCoveredGuardsModel

data class T_GuardListState(
    val coveredList: List<TeacherCoveredGuardsModel> = listOf(),
    val dailyList : List<TeacherCoveredGuardsModel> = listOf(),
    val idUser: String = "",
    val getGuardListExecuting: Boolean = false,
    val getIdUserEnd :Boolean = false
)