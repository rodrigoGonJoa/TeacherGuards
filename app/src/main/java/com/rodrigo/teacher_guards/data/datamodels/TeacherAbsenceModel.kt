package com.rodrigo.teacher_guards.data.datamodels

import com.rodrigo.teacher_guards.data.datamodels.enums.CourseEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.HourEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.SubjectEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum
import java.io.Serializable

data class TeacherAbsenceModel(
    val date_from: String = "",
    val date_to: String = "",
    var days_absent: List<DayAbsentModel> = listOf()
)

data class DayAbsentModel(
    val order: Int = 0,
    val date: String = "",
    val weekDay: WeekDayEnum = WeekDayEnum.ERROR,
    var absentSchedule: List<AbsentScheduleModel> = listOf()
)

data class AbsentScheduleModel(
    val hour: HourEnum = HourEnum.ERROR,
    val absent: Boolean = true,
    val course: CourseEnum = CourseEnum.ERROR,
    var taskRef: String = "",
    val subject: SubjectEnum = SubjectEnum.ERROR
) : Serializable{
    companion object {
        fun setTaskRef(
            dayAbsent: DayAbsentModel,
            absentScheduleModel: AbsentScheduleModel
        ): String {
            return dayAbsent.date + "_" + absentScheduleModel.hour + "_" + absentScheduleModel.course.name
        }
    }
}

