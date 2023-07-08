package com.rodrigo.teacher_guards.ui.teacher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.databinding.ActivityTeacherBinding
import com.rodrigo.teacher_guards.utils.Utils

class TeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom Navigation
        val navFragment = supportFragmentManager.findFragmentById(binding.teacherNavHostFragment.id) as NavHostFragment
        val navController = navFragment.navController
        val bottomNavigationView = binding.teacherBottomNavView
        bottomNavigationView.setupWithNavController(navController)

        // HideNavController
        hideNavController(navController)
        // Agrega un OnBackPressedCallback para manejar el botón hacia atrás
        setupBackPressedCallback(navController)
    }

    fun showSnackbar(message: String) {
        Utils.topSnackBar(findViewById(android.R.id.content), message).show()
    }

    private fun setupBackPressedCallback(navController: NavController) {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    // Si hay fragmentos en la pila, maneja el comportamiento predeterminado del botón "Atrás"
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                } else {
                    // Verifica si el NavController puede manejar el botón hacia atrás
                    if (!navController.popBackStack()) {
                        // Si el NavController no puede manejar el botón hacia atrás, muestra el diálogo de confirmación
                        showExitConfirmationDialog()
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun hideNavController(navController: NavController){
        navController.addOnDestinationChangedListener { tal, destination, _ ->
            when (destination.id) {
                R.id.t_DisplayGuardFragment,
                R.id.t_DisplayAbsenceFragment,
                R.id.t_TaskFragment
                -> {
                    binding.teacherBottomNavView.visibility = View.GONE
                }
                else -> {
                    binding.teacherBottomNavView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage("¿Desea salir de la aplicación?")
            .setPositiveButton("Sí") { _, _ ->
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}