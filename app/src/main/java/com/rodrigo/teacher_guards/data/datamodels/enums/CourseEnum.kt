package com.rodrigo.teacher_guards.data.datamodels.enums

import android.content.Context
import com.rodrigo.teacher_guards.R

enum class CourseEnum(val score: Int, val courseName: Int) {
    ERROR(0, R.string.course_error),
    COURSE1(4, R.string.course_1),
    COURSE2(4, R.string.course_2),
    COURSE3(4, R.string.course_3),
    COURSE4(4, R.string.course_4),
    COURSE5(3, R.string.course_5),
    COURSE6(3, R.string.course_6),
    COURSE7(2, R.string.course_7),
    COURSE8(2, R.string.course_8),
    COURSE9(1, R.string.course_9),
    COURSE10(1, R.string.course_10);

    fun getText(context: Context): String {
        return context.getString(this.courseName)
    }

    fun toCourseEnum(value: String, context: Context): CourseEnum {
        for (enumValue in CourseEnum.values()) {
            if (enumValue.getText(context) == value) {
                return enumValue
            }
        }
        return ERROR
    }
    companion object{
        fun obtenerCursoPorNombre(nombreCurso: String, context: Context): CourseEnum? {
            return CourseEnum.values().find { context.getString(it.courseName) == nombreCurso }
        }
    }
}