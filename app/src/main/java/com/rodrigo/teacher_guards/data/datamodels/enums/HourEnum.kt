package com.rodrigo.teacher_guards.data.datamodels.enums

import android.content.Context
import com.rodrigo.teacher_guards.R

enum class HourEnum(val order: Int, val hourName: Int) {
    ERROR(0, R.string.hour_error),
    HOUR1(1, R.string.hour_1),
    HOUR2(2, R.string.hour_2),
    HOUR3(3, R.string.hour_3),
    HOUR4(4, R.string.hour_4),
    HOUR5(5, R.string.hour_5),
    HOUR6(6, R.string.hour_6);

    fun getText(context: Context): String {
        return context.getString(this.hourName)
    }

    companion object {
        fun toHourEnum(value: String, context: Context): HourEnum {
            for (enumValue in HourEnum.values()) {
                if (enumValue.getText(context) == value) {
                    return enumValue
                }
            }
            return ERROR
        }

        fun toHourEnum(value: Int): HourEnum {
            for (enumValue in HourEnum.values()) {
                if (enumValue.order == value) {
                    return enumValue
                }
            }
            return ERROR
        }
    }
}