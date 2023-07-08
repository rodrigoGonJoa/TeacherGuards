package com.rodrigo.teacher_guards.ui.administrator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.databinding.ActivityAdminBinding
import com.rodrigo.teacher_guards.utils.Utils

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom Navigation
        val navFragment =
            supportFragmentManager.findFragmentById(binding.adminNavHostFragment.id) as NavHostFragment
        val navController = navFragment.navController
        val bottomNavigationView = binding.adminBottomNavView
        bottomNavigationView.setupWithNavController(navController)

        //hideNavController
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

    private fun hideNavController(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.a_DisplayAbsenceFragment,
                R.id.a_SetTeacherFragment,
                R.id.a_DisplayTeacherFragment,
                R.id.a_AddTeacherFragment
                -> { binding.adminBottomNavView.visibility = View.GONE }
                else -> { binding.adminBottomNavView.visibility = View.VISIBLE }
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