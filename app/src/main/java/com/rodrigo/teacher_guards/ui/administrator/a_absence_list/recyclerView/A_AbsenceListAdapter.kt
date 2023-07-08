package com.rodrigo.teacher_guards.ui.administrator.a_absence_list.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.AdminAbsenceListModel
import com.rodrigo.teacher_guards.databinding.FragmentAAbsenceListItemBinding

class A_AbsenceListAdapter(
    private var absenceList: List<AdminAbsenceListModel>,
    private val onClickItem: (String) -> Unit
) : RecyclerView.Adapter<A_AbsenceListAdapter.A_AbsenceListViewHolder>(){

    fun setAbsenceList(absences: List<AdminAbsenceListModel>) {
        absenceList = absences
    }

    inner class A_AbsenceListViewHolder(view: View) :RecyclerView.ViewHolder(view){
        private val binding = FragmentAAbsenceListItemBinding.bind(view)
        fun bind(
            absence: AdminAbsenceListModel,
            onClickItem: (String) -> Unit) {

            val fullName = absence.teacher_name + " " + absence.teacher_surname
            binding.lblNameSurname.text = fullName

            binding.root.setOnClickListener {
                onClickItem(absence.absence_id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): A_AbsenceListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return A_AbsenceListViewHolder(
            layoutInflater.inflate(
                R.layout.fragment_a_absence_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return absenceList.size
    }

    override fun onBindViewHolder(holder: A_AbsenceListViewHolder, position: Int) {
        val absence = absenceList[position]
        holder.bind(absence, onClickItem)
    }
}