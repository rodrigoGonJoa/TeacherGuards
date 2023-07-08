package com.rodrigo.teacher_guards.ui.administrator.a_teacher_list.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.Teacher
import com.rodrigo.teacher_guards.databinding.FragmentATeacherListItemBinding

class A_TeacherListAdapter(
    private var teacherList: List<Teacher>,
    private val onClickItem: (String) -> Unit,
) : RecyclerView.Adapter<A_TeacherListAdapter.A_TeacherListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): A_TeacherListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return A_TeacherListViewHolder(
            layoutInflater.inflate(
                R.layout.fragment_a_teacher_list_item,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: A_TeacherListViewHolder, position: Int) {
        val manga = teacherList[position]
        holder.bind(manga, onClickItem)
    }
    override fun getItemCount(): Int {
        return teacherList.size
    }

    fun setTeacherList(teachers: List<Teacher>) {
        teacherList = teachers
    }

    inner class A_TeacherListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FragmentATeacherListItemBinding.bind(view)

        fun bind(
            teacher: Teacher,
            onClickItem: (String) -> Unit
        ) {
            // Fields - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            val fullName = teacher.name + " " + teacher.surname
            binding.lblNameSurname.text = fullName

            // Actions - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
            binding.root.setOnClickListener {
                onClickItem(teacher.id)
            }
        }
    }
}