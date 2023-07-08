package com.rodrigo.teacher_guards.ui.teacher.t_guard_list.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.TeacherCoveredGuardsModel
import com.rodrigo.teacher_guards.databinding.FragmentTGuardListCoveredGuardItemBinding
import com.rodrigo.teacher_guards.utils.Utils

class T_GuardListFragmentCoveredAdapter(
    private var guardList : List<TeacherCoveredGuardsModel>,
    private val onClickItem : (String, String) -> Unit
) : RecyclerView.Adapter<T_GuardListFragmentCoveredAdapter.T_GuardListFragmentViewHolder>() {


    inner class T_GuardListFragmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FragmentTGuardListCoveredGuardItemBinding.bind(view)
        private val context = binding.root.context

        fun bind(
            guard: TeacherCoveredGuardsModel,
            onClickItem: (String, String) -> Unit) {
            val weekday = context.getString(guard.weekDay.weekDayName)
            val date = Utils.setDateWithSlash(guard.date)
            val hour = context.getString(guard.hour.hourName)
            val fullText = "$weekday, $date - $hour"
            binding.date.text = fullText

            binding.root.setOnClickListener {
                onClickItem(guard.taskRef, guard.date)
            }
        }
    }

    fun setGuardList(guards: List<TeacherCoveredGuardsModel>) {
        guardList = guards
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): T_GuardListFragmentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return T_GuardListFragmentViewHolder(
            layoutInflater.inflate(
                R.layout.fragment_t_guard_list_covered_guard_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return guardList.size
    }

    override fun onBindViewHolder(holder: T_GuardListFragmentViewHolder, position: Int) {
        val guard = guardList[position]
        holder.bind(guard, onClickItem)
    }
}