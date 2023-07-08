package com.rodrigo.teacher_guards.data.datamodels

import com.rodrigo.teacher_guards.data.datamodels.enums.HourEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum

data class TeacherCoveredGuardsModel(
    val date : String = "",
    val weekDay : WeekDayEnum = WeekDayEnum.ERROR,
    val hour : HourEnum = HourEnum.ERROR,
    var taskRef : String = "",
){
    fun setTaskRef(dayAbsent: DayAbsentModel, absentScheduleModel: AbsentScheduleModel){
        taskRef = dayAbsent.date + "_" + absentScheduleModel.hour + " " + absentScheduleModel.course.courseName
    }
}