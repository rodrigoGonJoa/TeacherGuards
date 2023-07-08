package com.rodrigo.teacher_guards.ui.teacher.t_display_absence

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.TaskModel
import com.rodrigo.teacher_guards.databinding.FragmentTTaskAbsenceBinding
import com.rodrigo.teacher_guards.utils.Utils
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.N)
class T_TaskFragment : Fragment() {

    private lateinit var binding: FragmentTTaskAbsenceBinding
    private val vm: T_DisplayAbsenceVM by viewModels { T_DisplayAbsenceVM.Factory }
    private val args: T_TaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTTaskAbsenceBinding.inflate(layoutInflater)
        initView()
        setListners()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
    }

    fun setListners(){
        binding.btnTaskSave.setOnClickListener {
            updateTask(createTask())
        }
        binding.btnTaskCancel.setOnClickListener {
            navigate()
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { uiState ->
                    if (uiState.updateTaskAction) {
                        vm.endAddTaskAction()
                        navigate()
                    }
                }
            }
        }
    }

    private fun navigate() {
        val action =
            T_TaskFragmentDirections.actionTTaskFragmentToTDisplayAbsenceFragment2(
                args.dateFrom,
                args.dateTo
            )
        findNavController().navigate(action)
    }


    private fun initView() {
        val schedule = args.schedule
        with(binding) {
            txtTaskAssignedTeacher.text = getString(R.string.t_task_absence_assigned_teacher)
            txtTaskCourse.text = getString(schedule.course.courseName)
            txtTaskSubject.text = getString(schedule.subject.subjectName)

            val composeDateHour = getString(args.weekday.weekDayName) +
                    ", " + Utils.setDateWithSlash(args.date) + " - " +
                    getString(schedule.hour.hourName)
            lblDateHour.text = composeDateHour
        }
    }

    private fun createTask() : TaskModel {
        val schedule = args.schedule
        return TaskModel(
            date = args.date,
            weekDay = args.weekday,
            hour = schedule.hour,
            absentTeacherName = "",
            absentTeacherSurname = "",
            course = schedule.course,
            subject = schedule.subject,
            description = binding.txtDescription.text.toString(),
        )
    }

    private fun updateTask(task : TaskModel){
        vm.cacheTask(task)
    }
}