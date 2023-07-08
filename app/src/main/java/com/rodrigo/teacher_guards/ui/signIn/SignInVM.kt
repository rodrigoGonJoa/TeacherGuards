package com.rodrigo.teacher_guards.ui.signIn

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.rodrigo.teacher_guards.data.dependencies.MyApplication
import com.rodrigo.teacher_guards.data.repository.FirestoreSignInRepository
import com.rodrigo.teacher_guards.data.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInVM(
    private val application: MyApplication,
    private val provideFirebaseAuth: GoogleSignInOptions,
    private val firestoreRepository: FirestoreSignInRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<SignInUIState> = MutableStateFlow(SignInUIState())
    val uiState: StateFlow<SignInUIState> = _uiState.asStateFlow()

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    var activityLauncher: ActivityLauncher? = null
    private lateinit var launcher: ActivityResultLauncher<Intent>

    fun initViewModel() {
        activityLauncher?.let { launcher = it.registerActivityResult(this::handleActivityResult) }
    }

    fun configureGoogle(activity: Activity) {
        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(activity, provideFirebaseAuth)
    }

    fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInIntent.putExtra("prompt", "select_account")

        activityLauncher?.launchActivityForResult(launcher, signInIntent)

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = !it.isLoading
                )
            }
        }
    }

    private fun handleActivityResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        var loginSuccess = false
        var email = ""
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                loginSuccess = true
                email = task.result.email!!

            }
        }
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = !it.isLoading,
                    loginSuccess = loginSuccess,
                    email = email,
                    loginActionExecuting = true
                )
            }
        }
    }

    fun endSignIn() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    checkingEmail = false,
                    isLoading = false,
                )
            }
        }
        firestoreRepository.getTeacherSignInData(_uiState.value.email) { name, email, rol ->
            saveDataToDataStore(name, email, rol)
        }
    }

    fun getRol() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loginActionExecuting = false,
                )
            }
        }
        firestoreRepository.getTeacherRol(_uiState.value.email) { rol ->
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        userRol =
                        if (rol == "admin" || rol == "teacher")
                            enumValueOf(rol.uppercase())
                        else (UserRol.ERROR),
                        checkingEmail = true
                    )
                }
            }
        }
    }

    fun logOutGoogle() {
        val googleSignInClient = GoogleSignIn.getClient(application, provideFirebaseAuth)
        googleSignInClient.signOut().addOnCompleteListener {
        }
    }

    private fun saveDataToDataStore(name: String, email: String, rol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.saveData(name, email, rol)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application

                return SignInVM(
                    application as MyApplication,
                    application.appcontainer.provideFirebaseAuth,
                    application.appcontainer.firestoreSignInRepository,
                    application.appcontainer.userPreferencesRepository
                ) as T
            }
        }
    }
}
