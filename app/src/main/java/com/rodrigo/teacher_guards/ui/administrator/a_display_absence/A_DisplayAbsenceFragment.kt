package com.rodrigo.teacher_guards.ui.administrator.a_display_absence

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rodrigo.teacher_guards.data.datamodels.AbsentScheduleModel
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum
import com.rodrigo.teacher_guards.databinding.FragmentADisplayAbsenceBinding
import com.rodrigo.teacher_guards.ui.administrator.a_display_absence.recyclerView.A_DisplayAbsenceAdapter
import kotlinx.coroutines.launch

class A_DisplayAbsenceFragment : Fragment() {

    private lateinit var binding: FragmentADisplayAbsenceBinding
    private val args: A_DisplayAbsenceFragmentArgs by navArgs()
    private val vm: A_DisplayAbsenceVM by viewModels { A_DisplayAbsenceVM.Factory }
    private lateinit var adapter: A_DisplayAbsenceAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentADisplayAbsenceBinding.inflate(layoutInflater)
        initRecView()
        loadRvData()
        setListeners()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        return binding.root
    }

    private fun setListeners() {
        binding.btnDisplayAbsenceSave.setOnClickListener {
            navigate()
        }
    }

    private fun navigate() {
       val action = A_DisplayAbsenceFragmentDirections.actionADisplayAbsenceFragmentToAAbsenceListFragment()
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
    }

    // todo lsiternes

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { uiState ->
                    if (uiState.loadStructure) {
                        initView(uiState)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView(uiState: A_DisplayAbsenceState) {
        adapter.setdaysAbsentList(uiState.teacherAbsence!!.days_absent)
        adapter.notifyDataSetChanged()
        vm.loadStructureExecuted()
    }

    private fun initRecView() {
        adapter = A_DisplayAbsenceAdapter(
            daysAbsentList = listOf(),
            onClickButton = { schedule, date, weekday -> onClickButton(schedule, date, weekday) }
        )
        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvDisplayAbsence.adapter = adapter
        binding.rvDisplayAbsence.layoutManager = layoutManager
    }

    private fun onClickButton(schedule: AbsentScheduleModel, date: String, weekday: WeekDayEnum) {
        val action =
            A_DisplayAbsenceFragmentDirections.actionADisplayAbsenceFragmentToASetTeacherFragment(
                schedule, date, weekday, args.idAbsence
            )
        findNavController().navigate(action)
    }

    private fun loadRvData() {
        vm.getAbsence(args.idAbsence)
    }

}


