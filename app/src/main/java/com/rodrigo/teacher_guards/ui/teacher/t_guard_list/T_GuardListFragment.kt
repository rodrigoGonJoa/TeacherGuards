package com.rodrigo.teacher_guards.ui.teacher.t_guard_list

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
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
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.databinding.FragmentTGuardListBinding
import com.rodrigo.teacher_guards.ui.administrator.a_display_absence.A_DisplayAbsenceState
import com.rodrigo.teacher_guards.ui.teacher.t_guard_list.recyclerView.T_GuardListFragmentCoveredAdapter
import com.rodrigo.teacher_guards.ui.teacher.t_guard_list.recyclerView.T_GuardListFragmentDailyAdapter
import com.rodrigo.teacher_guards.utils.Utils
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class T_GuardListFragment : Fragment() {

    private lateinit var binding: FragmentTGuardListBinding
    private val vm: T_GuardListVM by viewModels { T_GuardListVM.Factory }
    private lateinit var dailyAdapter: T_GuardListFragmentDailyAdapter
    private lateinit var coveredAdapter: T_GuardListFragmentCoveredAdapter
    private lateinit var dailyLayoutManager: LinearLayoutManager
    private lateinit var coveredLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vm.displayInitiator()
        binding = FragmentTGuardListBinding.inflate(layoutInflater)
        initRecViewCovered()
        initRecViewDaily()
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
                    if(uiState.getIdUserEnd){
                        vm.getGuards()
                    }
                    if(uiState.getGuardListExecuting){
                        initView(uiState)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView(uiState: T_GuardListState) {
        dailyAdapter.setGuardList(uiState.dailyList)
        coveredAdapter.setGuardList(uiState.coveredList)
        dailyAdapter.notifyDataSetChanged()
        coveredAdapter.notifyDataSetChanged()
        vm.getGuardListExecuted()
    }

    private fun initRecViewCovered() {
        coveredAdapter = T_GuardListFragmentCoveredAdapter(
            guardList = vm.uiState.value.coveredList,
            onClickItem = { idGuard, date -> onClickItem(idGuard, date) }
        )
        coveredLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvCoveredGuard.adapter = coveredAdapter
        binding.rvCoveredGuard.layoutManager = coveredLayoutManager
    }

    private fun initRecViewDaily() {
        dailyAdapter = T_GuardListFragmentDailyAdapter(
            guardList = vm.uiState.value.dailyList,
            onClickItem = { idGuard, date -> onClickItem(idGuard, date) }
        )
        dailyLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvGuardToday.adapter = dailyAdapter
        binding.rvGuardToday.layoutManager = dailyLayoutManager
    }

    private fun onClickItem(idGuard : String, date: String){
        val action = T_GuardListFragmentDirections.actionTGuardListFragmentToTDisplayGuardFragment(idGuard, date)
        findNavController().navigate(action)
    }
}