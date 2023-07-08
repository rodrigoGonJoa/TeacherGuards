package com.rodrigo.teacher_guards.ui.teacher.t_absence_list.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.TeacherAbsenceListModel
import com.rodrigo.teacher_guards.databinding.FragmentTAbsenceListItemBinding
import com.rodrigo.teacher_guards.utils.Utils

class T_AbsenceListAdapter(
    private var absenceList: List<TeacherAbsenceListModel>,
    private val onClickItem: (String) -> Unit
) : RecyclerView.Adapter<T_AbsenceListAdapter.T_AbsenceListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T_AbsenceListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return T_AbsenceListViewHolder(
            layoutInflater.inflate(
                R.layout.fragment_t_absence_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: T_AbsenceListViewHolder, position: Int) {
        val absence = absenceList[position]
        holder.bind(absence, onClickItem)
    }

    override fun getItemCount(): Int {
        return absenceList.size
    }

    fun setAbsenceList(absences: List<TeacherAbsenceListModel>) {
        absenceList = absences
    }

    inner class T_AbsenceListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FragmentTAbsenceListItemBinding.bind(view)
        private val context = binding.root.context

        fun bind(
            absence: TeacherAbsenceListModel,
            onClickItem: (String) -> Unit
        ) {
            // Fields - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            val fullText = context.getString(absence.weekDay.weekDayName) + ", " + Utils.setDateWithSlash(absence.dateFrom)
            binding.lblWeekdayDate.text = fullText

            // Actions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            binding.root.setOnClickListener {
                onClickItem(absence.absence_ref)
            }
        }
    }
}