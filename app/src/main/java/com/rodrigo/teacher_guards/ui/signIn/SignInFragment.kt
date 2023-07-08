package com.rodrigo.teacher_guards.ui.signIn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResult
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.databinding.FragmentSignInBinding
import com.rodrigo.teacher_guards.ui.administrator.AdminActivity
import com.rodrigo.teacher_guards.ui.teacher.TeacherActivity
import com.rodrigo.teacher_guards.utils.Utils
import kotlinx.coroutines.launch


class SignInFragment : Fragment(), ActivityLauncher {
    private val signInVM: SignInVM by viewModels { SignInVM.Factory }
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        setListeners()
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authFirebase()
        setCollectors()
    }

    private fun setListeners() {
        binding.button.setOnClickListener {
            signInVM.signInGoogle()
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInVM.uiState.collect {
                    if (it.loginActionExecuting) {
                        signInVM.getRol()
                    }
                    if(it.checkingEmail){
                        executeAction(it)
                    }
                }
            }
        }
    }

    private fun navigations(uiState: SignInUIState) {

        if (uiState.userRol == UserRol.TEACHER) {
            val intent = Intent(requireContext(), TeacherActivity::class.java)
            startActivity(intent)
        } else if (uiState.userRol == UserRol.ADMIN) {
            val intent = Intent(requireContext(), AdminActivity::class.java)
            startActivity(intent)
        }
        requireActivity().finish()
    }

    private fun executeAction(uiState: SignInUIState) {
        showLoadBar(uiState.isLoading)
        if (!uiState.loginSuccess || uiState.userRol == UserRol.ERROR) {
            Utils.topSnackBar(requireView(), getString(R.string.sign_in_snack_error)).show()
            signInVM.logOutGoogle()
        } else {
            navigations(uiState)
        }
        signInVM.endSignIn()
    }

    private fun authFirebase() {
        signInVM.activityLauncher = this
        signInVM.configureGoogle(requireActivity())
        signInVM.initViewModel()
    }

    private fun showLoadBar(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun initView(){
        binding.progressBar.visibility = View.GONE
    }


    override fun registerActivityResult(onResult: (ActivityResult) -> Unit): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult(), onResult)
    }
    override fun launchActivityForResult(launcher: ActivityResultLauncher<Intent>, intent: Intent) {
        launcher.launch(intent)
    }
}
