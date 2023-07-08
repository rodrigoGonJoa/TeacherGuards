package com.rodrigo.teacher_guards.data.datamodels

import com.rodrigo.teacher_guards.data.datamodels.enums.CourseEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.HourEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.SubjectEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum

data class TaskModel(
    val date : String = "",
    val weekDay : WeekDayEnum = WeekDayEnum.ERROR,
    val hour : HourEnum = HourEnum.ERROR, //
    val guardTeacherName : String = "",
    val guardTeacherSurname : String = "",
    var absentTeacherName : String = "",
    var absentTeacherSurname : String = "",
    val course : CourseEnum = CourseEnum.ERROR, //
    val subject : SubjectEnum = SubjectEnum.ERROR, //
    var description : String = "",
    val docRef: String = "",
)
