package com.rodrigo.teacher_guards.ui.administrator.a_profile

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
import com.rodrigo.teacher_guards.databinding.FragmentAProfileBinding
import com.rodrigo.teacher_guards.ui.signIn.SignInActivity
import com.rodrigo.teacher_guards.ui.teacher.t_profile.T_ProfileState
import kotlinx.coroutines.launch

class A_ProfileFragment : Fragment() {

    private lateinit var binding: FragmentAProfileBinding
    private val vm: A_ProfileVM by viewModels { A_ProfileVM.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAProfileBinding.inflate(layoutInflater)
        setListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
    }


    private fun setListeners(){
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
    private fun initViews(uiState: A_ProfileState) {
        binding.lblEmailUser.text = uiState.email
        binding.lblRol.text = uiState.rol
    }

    private fun navigations() {
        val intent = Intent(requireContext(), SignInActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}