package com.rodrigo.teacher_guards.ui.administrator.a_display_teacher

import android.os.Bundle
import android.util.Log
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
import com.rodrigo.teacher_guards.databinding.FragmentADisplayTeacherBinding
import kotlinx.coroutines.launch

class A_DisplayTeacherFragment : Fragment() {

    private lateinit var binding: FragmentADisplayTeacherBinding
    private val vm: A_DisplayTeachertVM by viewModels { A_DisplayTeachertVM.Factory }
    private val args: A_DisplayTeacherFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentADisplayTeacherBinding.inflate(layoutInflater)
        initViews()
        setListeners()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {}
        return binding.root
    }

    private fun setListeners() {
        binding.button2.setOnClickListener {
            navigate()
        }
    }

    private fun navigate() {
        val action =
            A_DisplayTeacherFragmentDirections.actionADisplayTeacherFragmentToATeacherListFragment()
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
                    setViews(uiState)
                }
            }
        }
    }

    private fun setViews(state: A_DisplayTeacherState) {
        binding.lblEmail.text = state.teacher?.email.toString()
        //binding.lblTeacherStatus.text = state.teacher?.asistanceStatus.toString()
        val name = state.teacher?.name
        val surname = state.teacher?.surname
        val nameSurname = "$name $surname"
        binding.lblNameSurname.text = nameSurname
    }

    private fun initViews() {
        vm.loadViewData(args.idTeacher)
    }
}