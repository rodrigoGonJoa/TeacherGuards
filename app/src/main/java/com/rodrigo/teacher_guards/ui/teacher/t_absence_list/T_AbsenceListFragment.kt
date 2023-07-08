package com.rodrigo.teacher_guards.ui.teacher.t_absence_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rodrigo.teacher_guards.databinding.FragmentTAbsenceListBinding
import com.rodrigo.teacher_guards.ui.administrator.a_display_absence.A_DisplayAbsenceState
import com.rodrigo.teacher_guards.ui.teacher.TeacherActivity
import com.rodrigo.teacher_guards.ui.teacher.t_absence_list.dialog.DateDialogFragment
import com.rodrigo.teacher_guards.ui.teacher.t_absence_list.recyclerView.T_AbsenceListAdapter
import com.rodrigo.teacher_guards.ui.teacher.t_display_absence.recyclerView.T_DisplayAbsenceAdapter
import com.rodrigo.teacher_guards.utils.Utils
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class T_AbsenceListFragment : Fragment() {

    private lateinit var binding: FragmentTAbsenceListBinding
    private val vm: T_AbsenceListVM by viewModels { T_AbsenceListVM.Factory }
    private val customDialog = DateDialogFragment()
    private lateinit var adapter: T_AbsenceListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTAbsenceListBinding.inflate(layoutInflater)
        setListeners()
        initRecView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       setCollectors()
    }

    private fun setListeners() {
        binding.btnAddAbsence.setOnClickListener {
            customDialog.setUiState(vm._uiState)
            customDialog.show(parentFragmentManager, "CustomDialog")
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect {

                    if(it.loadPreferences && it.idUser != ""){
                        vm.loadAbsenceList()
                    }
                    if(it.loadListEx){
                        updateRecView(it)
                        vm.absenceListLoaded()
                    }
                    if (it.actionSaveDates) {
                        navigates(it.dateFrom, it.dateTo)
                    }
                }
            }
        }
    }

    private fun navigates(dateFrom: String, dateTo: String) {
        findNavController().navigate(
            T_AbsenceListFragmentDirections.actionTAbsenceListFragmentToTDisplayAbsenceFragment(
                Utils.setDateWithoutSlash(dateFrom),
                Utils.setDateWithoutSlash(dateTo)
            )
        )
    }
    private fun initRecView() {
        adapter = T_AbsenceListAdapter(
            absenceList = vm.uiState.value.absenceList,
            onClickItem = { absenceId -> onClickItem(absenceId) }
        )
        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvTeacherAbsenceList.adapter = adapter
        binding.rvTeacherAbsenceList.layoutManager = layoutManager
    }

    private fun updateRecView(uiState: T_AbsenceListState) {
        adapter.setAbsenceList(uiState.absenceList)
        adapter.notifyDataSetChanged()
    }

    fun onClickItem(absenceId : String){


    }
}