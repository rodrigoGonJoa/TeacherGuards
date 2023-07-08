package com.rodrigo.teacher_guards.data.datamodels.enums

import android.content.Context
import com.rodrigo.teacher_guards.R

enum class SubjectEnum(val subjectName : Int){
    ERROR(R.string.subject_error),
    SUBJECT1(R.string.subject_1),
    SUBJECT2(R.string.subject_2),
    SUBJECT3(R.string.subject_3),
    SUBJECT4(R.string.subject_4),
    SUBJECT5(R.string.subject_5);

    fun getText(context: Context): String {
        return context.getString(this.subjectName)
    }
    companion object{
        fun toSubjectEnum(value: String, context: Context): SubjectEnum {
            for (enumValue in SubjectEnum.values()) {
                if (enumValue.getText(context) == value) {
                    return enumValue
                }
            }
            return ERROR
        }
    }

}