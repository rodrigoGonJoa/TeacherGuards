package com.rodrigo.teacher_guards.ui.teacher.t_display_guard

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
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.TaskModel
import com.rodrigo.teacher_guards.databinding.FragmentTDisplayGuardBinding
import com.rodrigo.teacher_guards.databinding.FragmentTProfileBinding
import com.rodrigo.teacher_guards.ui.teacher.t_display_absence.T_DisplayAbsenceVM
import com.rodrigo.teacher_guards.ui.teacher.t_display_absence.T_TaskFragmentArgs
import com.rodrigo.teacher_guards.utils.Utils
import kotlinx.coroutines.launch

class T_DisplayGuardFragment : Fragment() {

    private lateinit var binding: FragmentTDisplayGuardBinding
    private val args: T_DisplayGuardFragmentArgs by navArgs()
    private val vm: T_DisplayGuardVM by viewModels { T_DisplayGuardVM.Factory }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTDisplayGuardBinding.inflate(layoutInflater)
        vm.getGuard(args.idGuard, args.date)
        setListeners()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        return binding.root
    }

    private fun setListeners() {
        binding.btnTaskCancel.setOnClickListener {
            navigate()
        }
    }

    private fun navigate() {
        val action = T_DisplayGuardFragmentDirections.actionTDisplayGuardFragmentToTGuardListFragment()
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { uiState ->
                    if (uiState.guardSaved) {
                        initViews(uiState.guard)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(guard: TaskModel) {
        val weekday = getString(guard.weekDay.weekDayName)
        val date = Utils.setDateWithSlash(guard.date)
        val hour = getString(guard.hour.hourName)
        val topText = "$weekday, $date - $hour"
        binding.lblDateHour.text = topText

        binding.txtTaskAssignedTeacher.text =
            guard.absentTeacherName + " " + guard.absentTeacherSurname
        binding.txtTaskCourse.text = getString(guard.course.courseName)
        binding.txtTaskSubject.text = getString(guard.subject.subjectName)
        binding.description.setText(guard.description)
    }
}