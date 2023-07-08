package com.rodrigo.teacher_guards.ui.teacher.t_profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.databinding.FragmentSignInBinding
import com.rodrigo.teacher_guards.databinding.FragmentTProfileBinding
import com.rodrigo.teacher_guards.ui.administrator.a_absence_list.A_AbsenceListVM
import com.rodrigo.teacher_guards.ui.signIn.SignInActivity
import kotlinx.coroutines.launch

class T_ProfileFragment : Fragment() {

    private lateinit var binding: FragmentTProfileBinding
    private val vm: T_ProfileVM by viewModels { T_ProfileVM.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTProfileBinding.inflate(layoutInflater)
        setListners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
    }

    fun setListners(){
        binding.btnSignOut.setOnClickListener {
            vm.cerrarSesionGoogle()
        }
    }

    private fun setCollectors(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { uiState ->
                    if (uiState.logOutActionExecuting) {
                        navigations()
                    }
                    if(uiState.getDataEnd){
                        initViews(uiState)
                        vm.dataLoaded()
                    }
                }
            }
        }
    }

    private fun initViews(uiState: T_ProfileState) {
        binding.lblEmailUser.text = uiState.email
        binding.lblRol.text = uiState.rol
    }

    private fun navigations() {
        val intent = Intent(requireContext(), SignInActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}