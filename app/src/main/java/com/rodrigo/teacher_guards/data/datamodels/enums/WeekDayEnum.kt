package com.rodrigo.teacher_guards.data.datamodels.enums

import android.content.Context
import com.rodrigo.teacher_guards.R
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class WeekDayEnum(val weekDayName: Int) : Serializable {
    ERROR(R.string.day_error),
    DAY1(R.string.day_1),
    DAY2(R.string.day_2),
    DAY3(R.string.day_3),
    DAY4(R.string.day_4),
    DAY5(R.string.day_5);

    fun getText(context: Context): String {
        return context.getString(this.weekDayName)
    }

    companion object {
        fun fromString(value: String, context: Context): WeekDayEnum {
            for (enumValue in values()) {
                if (enumValue.getText(context) == value) {
                    return enumValue
                }
            }
            return ERROR
        }

        fun fromDate(fecha: String): WeekDayEnum {
            val formato = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val date = formato.parse(fecha)

            val calendar = Calendar.getInstance()
            calendar.time = date!!

            val numeroDiaSemana = calendar.get(Calendar.DAY_OF_WEEK)
            val nombreDiaSemana = when (numeroDiaSemana) {
                Calendar.MONDAY -> DAY1
                Calendar.TUESDAY -> DAY2
                Calendar.WEDNESDAY -> DAY3
                Calendar.THURSDAY -> DAY4
                Calendar.FRIDAY -> DAY5
                else -> ERROR
            }

            return nombreDiaSemana
        }
    }
}