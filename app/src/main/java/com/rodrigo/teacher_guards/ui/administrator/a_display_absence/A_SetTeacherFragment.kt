package com.rodrigo.teacher_guards.ui.administrator.a_display_absence

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.databinding.FragmentASetTeacherBinding
import com.rodrigo.teacher_guards.ui.administrator.AdminActivity
import com.rodrigo.teacher_guards.utils.Utils
import kotlinx.coroutines.launch

class A_SetTeacherFragment : Fragment() {

    private lateinit var binding: FragmentASetTeacherBinding
    private val vm: A_DisplayAbsenceVM by viewModels { A_DisplayAbsenceVM.Factory }
    private val args: A_SetTeacherFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentASetTeacherBinding.inflate(layoutInflater)
        initViews()
        serListeners()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
    }

    private fun serListeners() {
        binding.btnSaveAssign.setOnClickListener {
            val idDay = args.weekday.name + "_" + args.dayAbsentData.hour.order
            vm.saveTeacher(
                binding.ddTeacherInner.text.toString(),
                args.dayAbsentData,
                args.date,
                idDay
            )
        }
    }

    private fun showSnackBar(success: Boolean) {
        val activity = requireActivity() as AdminActivity
        if (success) {
            activity.showSnackbar("Se ha guardado")
        } else {
            activity.showSnackbar("No se ha guardado")
        }
    }

    private fun navigate() {
        try {
            val action =
                A_SetTeacherFragmentDirections.actionASetTeacherFragmentToADisplayAbsenceFragment(
                    args.idAbsence
                )
            findNavController().navigate(action)
        } finally {
            vm.addScoreExecuted()
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { uiState ->
                    if (uiState.dropItemsLoad) {
                        val idDay = args.weekday.name + "_" + args.dayAbsentData.hour.order
                        vm.loadDropList(idDay, requireContext())
                    }
                    if (uiState.dropItemsLoaded) {
                        setItemsDropDownClasses(uiState.dropItemMap)
                    }
                    if (uiState.addScoreExecuting) {
                        val success = uiState.successAddScore
                        if (success) {
                            showSnackBar(success)
                            navigate()
                        } else {
                            showSnackBar(success)
                        }
                    }
                }
            }
        }
    }

    private fun initViews() {
        val absenceData = args.dayAbsentData
        with(binding) {
            val txtDateHour =
                getString(args.weekday.weekDayName) + "," +
                        Utils.setDateWithSlash(args.date) + " - " +
                        getString(absenceData.hour.hourName)
            lblDateHour.text = txtDateHour

            txtTaskCourse.text = getString(absenceData.course.courseName)
            txtTaskSubject.text = getString(absenceData.subject.subjectName)
            txtScore.text = absenceData.course.score.toString()
        }
    }

    private fun setItemsDropDownClasses(teacherList: Map<String, String>) {
        val finalList = mutableListOf<String>()
        for (teacher in teacherList) {
            finalList.add(teacher.value)
        }

        val adaptador =
            ArrayAdapter(requireContext(), R.layout.fragment_a_set_teacher_drop_item, finalList)
        val autoCompleteTextView = (binding.ddTeacher.editText as AutoCompleteTextView)
        autoCompleteTextView.setAdapter(adaptador)
        autoCompleteTextView.threshold = 1
        autoCompleteTextView.requestFocus()
    }
}