package com.rodrigo.teacher_guards.ui.teacher.t_absence_list.dialog

import androidx.fragment.app.DialogFragment
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.rodrigo.teacher_guards.R
import com.rodrigo.teacher_guards.databinding.FragmentTAbsenceListDialogBinding
import com.rodrigo.teacher_guards.ui.teacher.t_absence_list.T_AbsenceListState
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar

class DateDialogFragment : DialogFragment() {

    private lateinit var uiState: MutableStateFlow<T_AbsenceListState>
    fun setUiState(state: MutableStateFlow<T_AbsenceListState>) {
        uiState = state
    }

    private val vm: DateDialogVM by viewModels { DateDialogVM.Companion.DateDialogVMFactory(uiState) }
    private lateinit var binding: FragmentTAbsenceListDialogBinding
    private lateinit var dateFromCalendar: Calendar
    private var dateToCalendar: Calendar? = null
    private lateinit var dialog: AlertDialog

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentTAbsenceListDialogBinding.inflate(layoutInflater)

        setListeners()

        val builder = AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.fragment_t_absence_list_dialog_lbl_title))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.fragment_t_absence_list_dialog_btn_confirm)) { _, _ ->
                vm.saveDates(
                    binding.editDateFrom.text.toString(),
                    binding.editDateTo.text.toString()
                )
            }
            .setNegativeButton(getString(R.string.fragment_t_absence_list_dialog_btn_cancel)) { dialog, _ ->
                clearVariables()
                dialog.cancel()
            }
        initView()
        dialog = builder.create()
        return dialog
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun openDatePicker(editText: EditText, isStartDate: Boolean) {
        val calInstance = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                actionResult(year, month, day, isStartDate, editText)
            },
            calInstance.get(Calendar.YEAR),
            calInstance.get(Calendar.MONTH),
            calInstance.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setListeners() {
        binding.editDateFrom.setOnClickListener {
            openDatePicker(binding.editDateFrom, true)
        }

        binding.editDateTo.setOnClickListener {
            openDatePicker(binding.editDateTo, false)
        }
    }

    private fun actionResult(
        year: Int,
        month: Int,
        day: Int,
        isStartDate: Boolean,
        editText: EditText
    ) {
        val selectedDateCalendar = Calendar.getInstance()
        val date = "$year/${month + 1}/$day"

        selectedDateCalendar.set(year, month, day)

        if (isStartDate) {
            dateFromCalendar = selectedDateCalendar
        } else {
            dateToCalendar = selectedDateCalendar
        }

        setEditText(editText, isStartDate, date)
    }

    private fun clearVariables() {
        binding.editDateTo.setText("")
        binding.editDateFrom.setText("")
        dateFromCalendar.clear()
        dateToCalendar?.clear()
    }

    private fun initView() {
        binding.editDateTo.isEnabled = false
    }

    private fun setEditText(editText: EditText, isStartDate: Boolean, date: String) {
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        if (isStartDate) {
            if ((dateToCalendar != null) && dateFromCalendar > dateToCalendar!!) {
                editText.setText(getString(R.string.fragment_t_absence_list_dialog_from_error))
                positiveButton.isEnabled = false
            } else {
                editText.setText(date)
                binding.editDateTo.isEnabled = true
            }
        } else {
            if (dateFromCalendar > dateToCalendar!!) {
                editText.setText(getString(R.string.fragment_t_absence_list_dialog_to_error))
                positiveButton.isEnabled = false
            } else {
                editText.setText(date)
                positiveButton.isEnabled = true
            }
        }
    }
}
