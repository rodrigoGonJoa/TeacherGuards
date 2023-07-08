package com.rodrigo.teacher_guards.ui.administrator.a_absence_list

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rodrigo.teacher_guards.databinding.FragmentAAbsenceListBinding
import com.rodrigo.teacher_guards.ui.administrator.a_absence_list.recyclerView.A_AbsenceListAdapter
import com.rodrigo.teacher_guards.ui.administrator.a_teacher_list.A_TeacherListState
import com.rodrigo.teacher_guards.ui.administrator.a_teacher_list.recyclerView.A_TeacherListAdapter
import com.rodrigo.teacher_guards.utils.Utils
import kotlinx.coroutines.launch
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
class A_AbsenceListFragment : Fragment() {

    private lateinit var binding: FragmentAAbsenceListBinding
    private val vm: A_AbsenceListVM by viewModels { A_AbsenceListVM.Factory }
    private lateinit var adapter: A_AbsenceListAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAAbsenceListBinding.inflate(layoutInflater)
        setListeners()
        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { uiState ->
                    if (uiState.absenceListUpdated) {
                        updateRevView(uiState)
                        vm.resetValues()
                    }
                }
            }
        }
    }

    private fun setListeners() {
        binding.editDate.setOnClickListener {
            openDatePicker()
        }
    }

    private fun initRecView() {
        adapter = A_AbsenceListAdapter(
            absenceList = listOf(),
            onClickItem = { idAbsence -> onClickItem(idAbsence) }
        )
        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun onClickItem(idAbsence: String) {
        val action =
            A_AbsenceListFragmentDirections.actionAAbsenceListFragmentToADisplayAbsenceFragment(
                idAbsence
            )
        findNavController().navigate(action)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateRevView(uiState: A_AbsenceListState) {
        adapter.setAbsenceList(uiState.absenceList)
        adapter.notifyDataSetChanged()
        changeLoadingStatus(uiState.loading)
    }

    @SuppressLint("SetTextI18n")
    private fun openDatePicker() {
        val calInstance = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val date = "$day/${month + 1}/$year"
                binding.editDate.setText(date)
                searchAbsences(date)
            },
            calInstance.get(Calendar.YEAR),
            calInstance.get(Calendar.MONTH),
            calInstance.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun searchAbsences(date: String) {
        val dateFormated = Utils.setEuDateToyyyyMMdd(date)
        vm.getAbsenceList(dateFormated)
    }

    private fun changeLoadingStatus(active: Boolean) {
        if (active) {
            binding.loadBar.visibility = View.VISIBLE
        } else {
            binding.loadBar.visibility = View.GONE
        }
    }

    private fun initViews() {
        binding.editDate.setText(Utils.setDateWithSlash(Utils.getToday()))
        initRecView()
    }
}