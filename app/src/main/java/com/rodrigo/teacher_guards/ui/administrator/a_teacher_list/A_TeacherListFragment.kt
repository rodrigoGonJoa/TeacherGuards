package com.rodrigo.teacher_guards.ui.administrator.a_teacher_list

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rodrigo.teacher_guards.databinding.FragmentATeacherListBinding
import com.rodrigo.teacher_guards.ui.administrator.a_teacher_list.recyclerView.A_TeacherListAdapter
import kotlinx.coroutines.launch

class A_TeacherListFragment : Fragment() {

    private lateinit var binding: FragmentATeacherListBinding
    private val vm: A_TeacherListVM by viewModels { A_TeacherListVM.Factory }
    private lateinit var adapter: A_TeacherListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentATeacherListBinding.inflate(layoutInflater)
        setListener()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecView()
        setCollectors()
    }

    private fun setListener() {
        binding.btnAddTeacher.setOnClickListener {
            findNavController().navigate(A_TeacherListFragmentDirections.actionATeacherListFragmentToAAddTeacherScheduleFragment())
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { uiState ->
                    updateRevView(uiState)
                }
            }
        }
    }

    private fun initRecView() {
        adapter = A_TeacherListAdapter(
            teacherList = listOf(),
            onClickItem = { idTeacher -> displayTeacher(idTeacher) }
        )
        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvTeacherList.adapter = adapter
        binding.rvTeacherList.layoutManager = layoutManager
    }

    private fun displayTeacher(idTeacher: String) {
        val action =
            A_TeacherListFragmentDirections.actionATeacherListFragmentToADisplayTeacherFragment(
                idTeacher
            )
        findNavController().navigate(action)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateRevView(uiState: A_TeacherListState) {
        vm.getTeachers()
        adapter.setTeacherList(uiState.teacherList)
        adapter.notifyDataSetChanged()
        showLoadBar(uiState.loading)
    }

    private fun showLoadBar(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}