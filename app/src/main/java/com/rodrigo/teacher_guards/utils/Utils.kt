package com.rodrigo.teacher_guards.utils

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class Utils {
    companion object {
        fun topSnackBar(requiredView: View, text: String): Snackbar {
            val snack = Snackbar.make(requiredView, text, Snackbar.LENGTH_SHORT)
            val view: View = snack.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            view.layoutParams = params
            return snack
        }
        fun setDateWithoutSlash(date: String): String {
            val formatoEntrada = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val formatoSalida = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val fechaFormateada = formatoSalida.format(formatoEntrada.parse(date)!!)
            return fechaFormateada
        }

        fun setDateWithSlash(date: String): String {
            val formatoEntrada = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val formatoSalida = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val fechaFormateada = formatoSalida.format(formatoEntrada.parse(date)!!)
            return fechaFormateada
        }

        fun setEuDateToyyyyMMdd(date: String):String{
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val date = inputFormat.parse(date)
            return outputFormat.format(date)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getToday(): String {
            val fechaActual = LocalDate.now()
            val formatoFecha = DateTimeFormatter.ofPattern("yyyyMMdd")
            return fechaActual.format(formatoFecha)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun obtenerDiasSemanaEntreFechas(
            context: Context,
            fechaInicio: String,
            fechaFin: String
        ): List<String> {
            val formatoFecha = android.icu.text.SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val calInicio = Calendar.getInstance()
            val calFin = Calendar.getInstance()
            val listaDiasSemana = mutableListOf<String>()

            calInicio.time = formatoFecha.parse(fechaInicio)
            calFin.time = formatoFecha.parse(fechaFin)

            val formatoDiaSemana = android.icu.text.SimpleDateFormat("EEEE", Locale.getDefault())

            while (calInicio.before(calFin) || calInicio == calFin) {
                val diaSemana = WeekDayEnum.fromString(
                    capitalize(formatoDiaSemana.format(calInicio.time)),
                    context
                ).toString()
                listaDiasSemana.add(diaSemana)
                calInicio.add(Calendar.DAY_OF_MONTH, 1)
            }

            return listaDiasSemana
        }
        fun capitalize(string: String): String {
            val primeraLetra = Character.toUpperCase(string[0])
            return primeraLetra + string.substring(1)
        }
    }
}