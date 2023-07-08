package com.rodrigo.teacher_guards.ui.teacher.t_display_absence.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.AbsentScheduleModel
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum
import com.rodrigo.teacher_guards.databinding.FragmentTDisplayAbsenceItemItemBinding

class T_DisplayAbsenceInnerAdapter(
    private var innerItems: List<AbsentScheduleModel>,
    private val onClickButton: (AbsentScheduleModel, String, WeekDayEnum) -> Unit,
    private val date: String,
    private val weekDay : WeekDayEnum
) : RecyclerView.Adapter<T_DisplayAbsenceInnerAdapter.T_DisplayAbsenceInnerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): T_DisplayAbsenceInnerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return T_DisplayAbsenceInnerViewHolder(
            layoutInflater.inflate(
                R.layout.fragment_t_display_absence_item_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: T_DisplayAbsenceInnerViewHolder, position: Int) {
        holder.bind(innerItems[position], onClickButton)
    }

    override fun getItemCount(): Int {
        return innerItems.size
    }

    inner class T_DisplayAbsenceInnerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FragmentTDisplayAbsenceItemItemBinding.bind(view)
        private val context = binding.root.context

        fun bind(
            absentScheduleModel: AbsentScheduleModel,
            onClickButton: (AbsentScheduleModel, String, WeekDayEnum) -> Unit
        ) {
            binding.cbDisplayAbsenceItemItemIsAbsense.isChecked = true
            binding.cbDisplayAbsenceItemItemIsAbsense.text = context.getString(R.string.t_display_absence_item_item_cb_absnet)
            binding.btnSetTeacher.text = context.getString(R.string.t_display_absence_item_item_btn_set_teacher)

            val textCompose =
                context.getString(absentScheduleModel.subject.subjectName) + ", " +
                        context.getString(absentScheduleModel.course.courseName)
            binding.txtDisplayAbsenceItemItemCourseSubject.text = textCompose

            binding.btnSetTeacher.setOnClickListener {
                onClickButton(absentScheduleModel, date, weekDay)
            }

            binding.cbDisplayAbsenceItemItemIsAbsense.setOnClickListener{
                binding.btnSetTeacher.isEnabled = binding.cbDisplayAbsenceItemItemIsAbsense.isChecked
            }
        }
    }
}