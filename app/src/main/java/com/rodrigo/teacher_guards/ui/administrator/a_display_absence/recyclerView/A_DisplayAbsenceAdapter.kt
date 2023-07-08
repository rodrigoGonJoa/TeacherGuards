package com.rodrigo.teacher_guards.ui.administrator.a_display_absence.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.AbsentScheduleModel
import com.rodrigo.teacher_guards.data.datamodels.DayAbsentModel
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum
import com.rodrigo.teacher_guards.databinding.FragmentADisplayAbsenceItemBinding
import com.rodrigo.teacher_guards.utils.Utils

class A_DisplayAbsenceAdapter(
    private var daysAbsentList: List<DayAbsentModel>,
    private val onClickButton: (AbsentScheduleModel, String, WeekDayEnum) -> Unit
) : RecyclerView.Adapter<A_DisplayAbsenceAdapter.A_DisplayAbsenceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): A_DisplayAbsenceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return A_DisplayAbsenceViewHolder(
            layoutInflater.inflate(
                R.layout.fragment_a_display_absence_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return daysAbsentList.size
    }

    override fun onBindViewHolder(holder: A_DisplayAbsenceViewHolder, position: Int) {
        val dayAbsent = daysAbsentList[position]
        holder.bind(dayAbsent)
    }

    fun setdaysAbsentList(daysAbsents: List<DayAbsentModel>) {
        daysAbsentList = daysAbsents
    }

    inner class A_DisplayAbsenceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FragmentADisplayAbsenceItemBinding.bind(view)

        val innerRecyclerView: RecyclerView = binding.rvDisplayAbsenceItemA
        val context = binding.root.context

        fun bind(
            dayAbsent: DayAbsentModel
        ) {
            val dateCompose =
                context.getString(dayAbsent.weekDay.weekDayName) + ", " + Utils.setDateWithSlash(
                    dayAbsent.date
                )
            binding.txtDisplayAbsenceItemDateA.text = dateCompose

            innerRecyclerView.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            innerRecyclerView.adapter = A_DisplayAbsenceInnerAdapter(
                dayAbsent.absentSchedule,
                onClickButton,
                dayAbsent.date,
                dayAbsent.weekDay
            )
        }
    }
}