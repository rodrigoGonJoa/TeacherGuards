package com.rodrigo.teacher_guards.ui.administrator.a_display_absence.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.AbsentScheduleModel
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum
import com.rodrigo.teacher_guards.databinding.FragmentADisplayAbsenceItemItemBinding

class A_DisplayAbsenceInnerAdapter(
    private var innerItems: List<AbsentScheduleModel>,
    private val onClickButton: (AbsentScheduleModel, String, WeekDayEnum) -> Unit,
    private val date: String,
    private val weekDay: WeekDayEnum
) : RecyclerView.Adapter<A_DisplayAbsenceInnerAdapter.A_DisplayAbsenceInnerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): A_DisplayAbsenceInnerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return A_DisplayAbsenceInnerViewHolder(
            layoutInflater.inflate(
                R.layout.fragment_a_display_absence_item_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: A_DisplayAbsenceInnerViewHolder, position: Int) {
        holder.bind(innerItems[position], onClickButton)
    }

    override fun getItemCount(): Int {
        return innerItems.size
    }

    inner class A_DisplayAbsenceInnerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = FragmentADisplayAbsenceItemItemBinding.bind(view)
        private val context = binding.root.context

        fun bind(

            absentScheduleModel: AbsentScheduleModel,
            onClickButton: (AbsentScheduleModel, String, WeekDayEnum) -> Unit
        ) {
            binding.cbDisplayAbsenceItemItemIsAssignedA.isChecked = false
            binding.btnSetTeacherA.text =
                context.getString(R.string.t_display_absence_item_item_btn_set_teacher)

            val textCompose =
                context.getString(absentScheduleModel.subject.subjectName) + ", " +
                        context.getString(absentScheduleModel.course.courseName)
            binding.txtDisplayAbsenceItemItemCourseSubjectA.text = textCompose

            binding.btnSetTeacherA.setOnClickListener {
                onClickButton(absentScheduleModel, date, weekDay)
            }
        }
    }
}