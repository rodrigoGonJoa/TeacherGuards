package com.rodrigo.teacher_guards.data.datamodels.Structure

import android.content.Context
import com.google.firebase.firestore.Exclude
import com.rodrigo.teacher_guards.data.datamodels.enums.CourseEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum

data class TeacherGetScheduleDay(
    val day: String = "",
    val class1: String = "",
    val class2: String = "",
    val class3: String = "",
    val class4: String = "",
    val class5: String = "",
    val class6: String = "",
    val subject1: String = "",
    val subject2: String = "",
    val subject3: String = "",
    val subject4: String = "",
    val subject5: String = "",
    val subject6: String = ""
) {
    @Exclude
    fun obtenerClase(numero: Int): String {
        val propertyName = "class$numero"
        return try {
            val property = TeacherGetScheduleDay::class.java.getDeclaredField(propertyName)
            property.isAccessible = true
            val value = property.get(this) as? String
            value ?: "" // Valor predeterminado si el valor es nulo
        } catch (e: Exception) {
            ""
        }
    }
    @Exclude
    fun obtenerSubject(numero: Int): String {
        val propertyName = "subject$numero"
        return try {
            val property = TeacherGetScheduleDay::class.java.getDeclaredField(propertyName)
            property.isAccessible = true
            val value = property.get(this) as? String
            value ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    @Exclude
    fun getEmptyClasses(): List<String> {
        val emptyClasses = mutableListOf<String>()
        if (class1 == CourseEnum.ERROR.toString()) {
            emptyClasses.add(day + "_1")
        }
        if (class2 == CourseEnum.ERROR.toString()) {
            emptyClasses.add(day + "_2")
        }
        if (class3 == CourseEnum.ERROR.toString()) {
            emptyClasses.add(day + "_3")
        }
        if (class4 == CourseEnum.ERROR.toString()) {
            emptyClasses.add(day + "_4")
        }
        if (class5 == CourseEnum.ERROR.toString()) {
            emptyClasses.add(day + "_5")
        }
        if (class6 == CourseEnum.ERROR.toString()) {
            emptyClasses.add(day + "_6")
        }
        return emptyClasses
    }

    override fun toString(): String {
        return "TeacherGetScheduleDay(day='$day', class1='$class1', class2='$class2', class3='$class3', class4='$class4', class5='$class5', class6='$class6', subject1='$subject1', subject2='$subject2', subject3='$subject3', subject4='$subject4', subject5='$subject5', subject6='$subject6')"
    }

}

data class TeacherGetSchedule(
    var day1: TeacherGetScheduleDay,
    var day2: TeacherGetScheduleDay,
    var day3: TeacherGetScheduleDay,
    var day4: TeacherGetScheduleDay,
    var day5: TeacherGetScheduleDay
) {

    fun obtenerDia(numero: Int): TeacherGetScheduleDay? {
        return when (numero) {
            0 -> day1
            1 -> day2
            2 -> day3
            3 -> day4
            4 -> day5
            else -> null
        }
    }

    fun obtenerDiaPorNombre(nombre: String, context :Context): TeacherGetScheduleDay? {
        return when (nombre) {
            WeekDayEnum.DAY1.toString() -> day1
            WeekDayEnum.DAY2.toString() -> day2
            WeekDayEnum.DAY3.toString() -> day3
            WeekDayEnum.DAY4.toString() -> day4
            WeekDayEnum.DAY5.toString() -> day5
            else -> null
        }
    }

    override fun toString(): String {
        return "TeacherGetSchedule(day1=$day1, day2=$day2, day3=$day3, day4=$day4, day5=$day5)"
    }
}
