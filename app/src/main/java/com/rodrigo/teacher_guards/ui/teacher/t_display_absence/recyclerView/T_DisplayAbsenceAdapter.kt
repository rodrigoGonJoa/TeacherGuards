package com.rodrigo.teacher_guards.ui.teacher.t_display_absence.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.AbsentScheduleModel
import com.rodrigo.teacher_guards.data.datamodels.DayAbsentModel
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum
import com.rodrigo.teacher_guards.databinding.FragmentTDisplayAbsenceItemBinding
import com.rodrigo.teacher_guards.utils.Utils

class T_DisplayAbsenceAdapter(
    private var daysAbsentList: List<DayAbsentModel>,
    private val onClickButton: (AbsentScheduleModel, String, WeekDayEnum) -> Unit
) : RecyclerView.Adapter<T_DisplayAbsenceAdapter.T_DisplayAbsenceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T_DisplayAbsenceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return T_DisplayAbsenceViewHolder(
            layoutInflater.inflate(
                R.layout.fragment_t_display_absence_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return daysAbsentList.size
    }

    override fun onBindViewHolder(holder: T_DisplayAbsenceViewHolder, position: Int) {
        val dayAbsent = daysAbsentList[position]
        holder.bind(dayAbsent)
    }

    fun setdaysAbsentList(daysAbsents: List<DayAbsentModel>) {
        daysAbsentList = daysAbsents
    }

    inner class T_DisplayAbsenceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FragmentTDisplayAbsenceItemBinding.bind(view)

        val innerRecyclerView: RecyclerView = binding.rvDisplayAbsenceItem
        val context = binding.root.context

        fun bind(
            dayAbsent: DayAbsentModel
        ) {
            val dateCompose =
                context.getString(dayAbsent.weekDay.weekDayName) + ", " + Utils.setDateWithSlash(
                    dayAbsent.date
                )
            binding.txtDisplayAbsenceItemDate.text = dateCompose

            innerRecyclerView.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            innerRecyclerView.adapter = T_DisplayAbsenceInnerAdapter(
                dayAbsent.absentSchedule,
                onClickButton,
                dayAbsent.date,
                dayAbsent.weekDay
            )
        }
    }
}