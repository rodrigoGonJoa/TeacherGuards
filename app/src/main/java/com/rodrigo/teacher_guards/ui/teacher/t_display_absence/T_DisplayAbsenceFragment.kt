package com.rodrigo.teacher_guards.ui.teacher.t_display_absence

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.AbsentScheduleModel
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum
import com.rodrigo.teacher_guards.databinding.FragmentTDisplayAbsenceBinding
import com.rodrigo.teacher_guards.ui.teacher.TeacherActivity
import com.rodrigo.teacher_guards.ui.teacher.t_display_absence.recyclerView.T_DisplayAbsenceAdapter
import com.rodrigo.teacher_guards.utils.Utils
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
class T_DisplayAbsenceFragment : Fragment() {

    private lateinit var binding: FragmentTDisplayAbsenceBinding
    private val args: T_DisplayAbsenceFragmentArgs by navArgs()
    private val vm: T_DisplayAbsenceVM by viewModels { T_DisplayAbsenceVM.Factory }
    private lateinit var adapter: T_DisplayAbsenceAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm.displayInitiator()
        binding = FragmentTDisplayAbsenceBinding.inflate(layoutInflater)
        setListeners()
        initRecView()
        loadView()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
    }

    private fun setListeners() {
        binding.btnDisplayAbsenceCancel.setOnClickListener {
            navigate(false)
        }
        binding.btnDisplayAbsenceSave.setOnClickListener {
            saveAbsence()
        }
    }

    private fun saveAbsence() {
        vm.saveAbsence()
    }

    private fun navigate(success: Boolean) {
        val activity = requireActivity() as TeacherActivity
        if (!success) {
            activity.showSnackbar(getString(R.string.t_display_absence_snack_error))
        } else {
            activity.showSnackbar(getString(R.string.t_display_absence_snack_saved))
        }
        findNavController().navigate(T_DisplayAbsenceFragmentDirections.actionTDisplayAbsenceFragmentToTAbsenceListFragment())
    }


    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { uiState ->
                    if (uiState.loadStructure) {
                        initView(uiState)
                    }
                    if (uiState.cacheTask != null) {
                        vm.updateTask(uiState.cacheTask)
                    }
                    if (uiState.saveAbsenceEnd) {
                        if (uiState.saveAbsenceSuccess) {
                            navigate(true)
                        }
                        Utils.topSnackBar(
                            requireView(),
                            getString(R.string.t_display_absence_snack_error)
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView(uiState: T_DisplayAbsenceState) {
        adapter.setdaysAbsentList(uiState.teacherAbsence!!.days_absent)
        adapter.notifyDataSetChanged()
    }

    private fun initRecView() {
        adapter = T_DisplayAbsenceAdapter(
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
            T_DisplayAbsenceFragmentDirections.actionTDisplayAbsenceFragmentToTTaskFragment(date,schedule,weekday,args.dateFrom,args.dateTo)

        findNavController().navigate(action)
    }


    private fun loadView() {
        vm.loadStructure(args.dateFrom, args.dateTo, requireContext())
    }
}