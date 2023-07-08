package com.rodrigo.teacher_guards.ui.teacher.t_guard_list.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.DayAbsentModel
import com.rodrigo.teacher_guards.data.datamodels.TeacherCoveredGuardsModel
import com.rodrigo.teacher_guards.databinding.FragmentTGuardListDailyGuardItemBinding

class T_GuardListFragmentDailyAdapter(
    private var guardList: List<TeacherCoveredGuardsModel>,
    private val onClickItem: (String, String) -> Unit
) : RecyclerView.Adapter<T_GuardListFragmentDailyAdapter.T_GuardListFragmentViewHolder>()
{
    fun setGuardList(guards: List<TeacherCoveredGuardsModel>) {
        guardList = guards
    }

    inner class T_GuardListFragmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FragmentTGuardListDailyGuardItemBinding.bind(view)
        private val context = binding.root.context

        fun bind(
            guard: TeacherCoveredGuardsModel,
            onClickItem: (String, String) -> Unit
        ) {
            binding.lblNameSurname.text = context.getString(guard.hour.hourName)

            binding.root.setOnClickListener {
                onClickItem(guard.taskRef, guard.date)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): T_GuardListFragmentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return T_GuardListFragmentViewHolder(
            layoutInflater.inflate(
                R.layout.fragment_t_guard_list_daily_guard_item,
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