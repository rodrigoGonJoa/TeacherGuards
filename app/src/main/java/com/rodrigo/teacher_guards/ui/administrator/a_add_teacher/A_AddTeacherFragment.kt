package com.rodrigo.teacher_guards.ui.administrator.a_add_teacher

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
import com.google.android.material.textfield.TextInputLayout
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.data.datamodels.Teacher
import com.rodrigo.teacher_guards.data.datamodels.Structure.TeacherGetSchedule
import com.rodrigo.teacher_guards.data.datamodels.Structure.TeacherGetScheduleDay
import com.rodrigo.teacher_guards.data.datamodels.enums.CourseEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.SubjectEnum
import com.rodrigo.teacher_guards.data.datamodels.enums.WeekDayEnum
import com.rodrigo.teacher_guards.databinding.FragmentAAddTeacherBinding
import com.rodrigo.teacher_guards.utils.Utils
import kotlinx.coroutines.launch

class A_AddTeacherFragment : Fragment() {

    private lateinit var binding: FragmentAAddTeacherBinding
    private val vm: A_AddTeacherVM by viewModels { A_AddTeacherVM.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAAddTeacherBinding.inflate(layoutInflater)
        setListener()
        setViews()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){}
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
        loadItemsDropDown()
    }

    private fun setListener() {
        binding.btnSave.setOnClickListener {
            if (!vm.uiState.value.loading) {
                val teacher = getPersonalData()
                val schedule = getScheduleData()
                vm.addTeacher(teacher, schedule)
            }
        }
        binding.btnCancel.setOnClickListener {
            val action = A_AddTeacherFragmentDirections.actionAAddTeacherFragmentToATeacherListFragment()
            findNavController().navigate(action)
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { uiState ->
                    updateLoadingVisibility(uiState)
                    if (uiState.teacherAddExecuting) {
                        alertTeacherAdded(uiState)
                    }
                }
            }
        }
    }

    private fun alertTeacherAdded(uiState: A_AddTeacherState) {
        if (uiState.teacherAdded) {
            Utils.topSnackBar(requireView(), getString(R.string.a_add_teacher_snack_added)).show()
        } else {
            Utils.topSnackBar(requireView(), getString(R.string.a_add_teacher_snack_error)).show()
        }
        vm.addTeacherExecuted()
        clearData()
    }

    private fun updateLoadingVisibility(uiState: A_AddTeacherState) {
        if (uiState.loading) {
            binding.loadCircle.visibility = View.VISIBLE
        } else {
            binding.loadCircle.visibility = View.GONE
        }
    }

    private fun setViews() {
        binding.loadCircle.visibility = View.GONE
    }

    private fun clearData() {
        with(binding) {
            editName.setText("")
            editSurname.setText("")
            editEmail.setText("")

            val classFields = arrayOf(
                editMonday1Class, editMonday2Class, editMonday3Class,
                editMonday4Class, editMonday5Class, editMonday6Class,
                editTuesday1Class, editTuesday2Class, editTuesday3Class,
                editTuesday4Class, editTuesday5Class, editTuesday6Class,
                editWednesday1Class, editWednesday2Class, editWednesday3Class,
                editWednesday4Class, editWednesday5Class, editWednesday6Class,
                editThursday1Class, editThursday2Class, editThursday3Class,
                editThursday4Class, editThursday5Class, editThursday6Class,
                editFriday1Class, editFriday2Class, editFriday3Class,
                editFriday4Class, editFriday5Class, editFriday6Class
            )

            val subjectFields = arrayOf(
                editMonday1Subject, editMonday2Subject, editMonday3Subject,
                editMonday4Subject, editMonday5Subject, editMonday6Subject,
                editTuesday1Subject, editTuesday2Subject, editTuesday3Subject,
                editTuesday4Subject, editTuesday5Subject, editTuesday6Subject,
                editWednesday1Subject, editWednesday2Subject, editWednesday3Subject,
                editWednesday4Subject, editWednesday5Subject, editWednesday6Subject,
                editThursday1Subject, editThursday2Subject, editThursday3Subject,
                editThursday4Subject, editThursday5Subject, editThursday6Subject,
                editFriday1Subject, editFriday2Subject, editFriday3Subject,
                editFriday4Subject, editFriday5Subject, editFriday6Subject
            )

            classFields.forEach { it.editText?.setText("") }
            subjectFields.forEach { it.editText?.setText("") }
        }
    }

    private fun getPersonalData(): Teacher {
        val teacher = Teacher(
            name = binding.editName.text.toString(),
            surname = binding.editSurname.text.toString(),
            email = binding.editEmail.text.toString()
        )
        teacher.setEmailId()
        return teacher
    }

    private fun createTeacherGetScheduleDay(
        day: String,
        vararg bindings: TextInputLayout
    ): TeacherGetScheduleDay {
        val classEditTexts = bindings.sliceArray(0..5).mapNotNull { it.editText }
        val subjectEditTexts = bindings.sliceArray(6..11).mapNotNull { it.editText }

        val classStrings = classEditTexts.map {
            CourseEnum.obtenerCursoPorNombre(
                it.text.toString(),
                requireContext()
            ).toString()
        }
        val subjectStrings = subjectEditTexts.map {
            SubjectEnum.toSubjectEnum(it.text.toString(), requireContext()).toString()
        }

        return TeacherGetScheduleDay(
            day,
            classStrings[0],
            classStrings[1],
            classStrings[2],
            classStrings[3],
            classStrings[4],
            classStrings[5],
            subjectStrings[0],
            subjectStrings[1],
            subjectStrings[2],
            subjectStrings[3],
            subjectStrings[4],
            subjectStrings[5]
        )
    }

    private fun getScheduleData(): TeacherGetSchedule {
        with(binding) {
            val day1 = createTeacherGetScheduleDay(
                WeekDayEnum.DAY1.toString(),
                editMonday1Class, editMonday2Class, editMonday3Class,
                editMonday4Class, editMonday5Class, editMonday6Class,
                editMonday1Subject, editMonday2Subject, editMonday3Subject,
                editMonday4Subject, editMonday5Subject, editMonday6Subject
            )

            val day2 = createTeacherGetScheduleDay(
                WeekDayEnum.DAY2.toString(),
                editTuesday1Class, editTuesday2Class, editTuesday3Class,
                editTuesday4Class, editTuesday5Class, editTuesday6Class,
                editTuesday1Subject, editTuesday2Subject, editTuesday3Subject,
                editTuesday4Subject, editTuesday5Subject, editTuesday6Subject
            )

            val day3 = createTeacherGetScheduleDay(
                WeekDayEnum.DAY3.toString(),
                editWednesday1Class, editWednesday2Class, editWednesday3Class,
                editWednesday4Class, editWednesday5Class, editWednesday6Class,
                editWednesday1Subject, editWednesday2Subject, editWednesday3Subject,
                editWednesday4Subject, editWednesday5Subject, editWednesday6Subject
            )

            val day4 = createTeacherGetScheduleDay(
                WeekDayEnum.DAY4.toString(),
                editThursday1Class, editThursday2Class, editThursday3Class,
                editThursday4Class, editThursday5Class, editThursday6Class,
                editThursday1Subject, editThursday2Subject, editThursday3Subject,
                editThursday4Subject, editThursday5Subject, editThursday6Subject
            )

            val day5 = createTeacherGetScheduleDay(
                WeekDayEnum.DAY5.toString(),
                editFriday1Class, editFriday2Class, editFriday3Class,
                editFriday4Class, editFriday5Class, editFriday6Class,
                editFriday1Subject, editFriday2Subject, editFriday3Subject,
                editFriday4Subject, editFriday5Subject, editFriday6Subject
            )

            return TeacherGetSchedule(day1, day2, day3, day4, day5)
        }
    }

    private fun setItemsDropDownClasses(textInputLayout: TextInputLayout) {
        val tempCourses = CourseEnum.values().toList()
        val courseList = mutableListOf<String>()

        for (c in tempCourses) {
            courseList.add(c.getText(requireContext()))
        }
        val adaptador =
            ArrayAdapter(requireContext(), R.layout.fragment_a_add_teacher_drop_item, courseList)
        val autoCompleteTextView = (textInputLayout.editText as AutoCompleteTextView)
        autoCompleteTextView.setAdapter(adaptador)
        autoCompleteTextView.threshold = 1
        autoCompleteTextView.requestFocus()
    }

    private fun setItemsDropDownSubjects(textInputLayout: TextInputLayout) {
        val tempSubjects = SubjectEnum.values().toList()
        val subjectList = mutableListOf<String>()

        for (c in tempSubjects) {
            subjectList.add(c.getText(requireContext()))
        }
        val adaptador =
            ArrayAdapter(requireContext(), R.layout.fragment_a_add_teacher_drop_item, subjectList)
        val autoCompleteTextView = (textInputLayout.editText as AutoCompleteTextView)
        autoCompleteTextView.setAdapter(adaptador)
        autoCompleteTextView.threshold = 1
        autoCompleteTextView.requestFocus()
    }

    private fun loadItemsDropDown() {
        with(binding) {
            val classBindings = arrayOf(
                editMonday1Class, editMonday2Class, editMonday3Class,
                editMonday4Class, editMonday5Class, editMonday6Class,
                editTuesday1Class, editTuesday2Class, editTuesday3Class,
                editTuesday4Class, editTuesday5Class, editTuesday6Class,
                editWednesday1Class, editWednesday2Class, editWednesday3Class,
                editWednesday4Class, editWednesday5Class, editWednesday6Class,
                editThursday1Class, editThursday2Class, editThursday3Class,
                editThursday4Class, editThursday5Class, editThursday6Class,
                editFriday1Class, editFriday2Class, editFriday3Class,
                editFriday4Class, editFriday5Class, editFriday6Class
            )

            val subjectBindings = arrayOf(
                editMonday1Subject, editMonday2Subject, editMonday3Subject,
                editMonday4Subject, editMonday5Subject, editMonday6Subject,
                editTuesday1Subject, editTuesday2Subject, editTuesday3Subject,
                editTuesday4Subject, editTuesday5Subject, editTuesday6Subject,
                editWednesday1Subject, editWednesday2Subject, editWednesday3Subject,
                editWednesday4Subject, editWednesday5Subject, editWednesday6Subject,
                editThursday1Subject, editThursday2Subject, editThursday3Subject,
                editThursday4Subject, editThursday5Subject, editThursday6Subject,
                editFriday1Subject, editFriday2Subject, editFriday3Subject,
                editFriday4Subject, editFriday5Subject, editFriday6Subject
            )

            classBindings.forEach(::setItemsDropDownClasses)
            subjectBindings.forEach(::setItemsDropDownSubjects)
        }
    }
}
