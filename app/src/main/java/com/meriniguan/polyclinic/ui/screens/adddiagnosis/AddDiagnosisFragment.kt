package com.meriniguan.polyclinic.ui.screens.adddiagnosis

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.meriniguan.polyclinic.MainActivity
import com.meriniguan.polyclinic.R
import com.meriniguan.polyclinic.databinding.FragmentAddDiagnosisBinding
import com.meriniguan.polyclinic.databinding.FragmentAddPatientBinding
import com.meriniguan.polyclinic.ui.screens.addpatients.AddPatientViewModel
import com.meriniguan.polyclinic.ui.screens.addpatients.AddPatientsFragmentDirections
import com.meriniguan.polyclinic.utils.getDateString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDiagnosisFragment : Fragment(R.layout.fragment_add_diagnosis) {

    private lateinit var binding: FragmentAddDiagnosisBinding
    private val viewModel by viewModels<AddDiagnosisViewModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddDiagnosisBinding.bind(view)
        (requireActivity() as MainActivity).supportActionBar?.title = "Добавить диагноз"
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            deceaseET.setOnTouchListener { v, event ->
                try {
                    if (event!!.action == MotionEvent.ACTION_DOWN) {
                        showSelectDeceaseDialog()
                    }
                } catch (_: Exception) { }
                true
            }
            patientET.setOnTouchListener { v, event ->
                try {
                    if (event!!.action == MotionEvent.ACTION_DOWN) {
                        showSelectPatientDialog()
                    }
                } catch (_: Exception) { }
                true
            }
            doctorET.setOnTouchListener { v, event ->
                try {
                    if (event!!.action == MotionEvent.ACTION_DOWN) {
                        showSelectDoctorDialog()
                    }
                } catch (_: Exception) { }
                true
            }
            if (viewModel.start != 0.toLong()) {
                startET.setText(getDateString(viewModel.start))
            }
            startET.setOnTouchListener { v, event ->
                try {
                    if (event!!.action == MotionEvent.ACTION_DOWN) {
                        showPickStartDateDialog()
                    }
                } catch (_: Exception) { }
                true
            }
            if (viewModel.ambNeeded) {
                ambYes.isChecked = true
            } else {
                ambNo.isChecked = true
            }
            if (viewModel.onRecord) {
                recordYes.isChecked = true
            } else {
                recordNo.isChecked = true
            }
            periodET.addTextChangedListener {
                viewModel.period = it.toString().toInt()
            }
            ambYes.setOnClickListener {
                viewModel.ambNeeded = true
            }
            ambNo.setOnClickListener {
                viewModel.ambNeeded = false
            }
            recordYes.setOnClickListener {
                viewModel.onRecord = true
            }
            recordNo.setOnClickListener {
                viewModel.onRecord = false
            }

            doneFab.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        setFragmentResultListener("dateReqK") { _, bundle ->
            val milliseconds = bundle.getLong("dateResK")
            viewModel.start = milliseconds
            binding.startET.setText(getDateString(milliseconds))
        }

        viewModel.updateDeceaseEvent.observe(viewLifecycleOwner) {
            if (viewModel.decease != null) {
                binding.deceaseET.setText(viewModel.decease!!.name)
            }
        }
        viewModel.updatePatientEvent.observe(viewLifecycleOwner) {
            if (viewModel.patient != null) {
                binding.patientET.setText(viewModel.patient!!.name)
            }
        }
        viewModel.updateDoctorEvent.observe(viewLifecycleOwner) {
            if (viewModel.doctor != null) {
                binding.doctorET.setText(viewModel.doctor!!.name)
            }
        }
    }

    private fun showSelectDeceaseDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Болезнь:")

        val conditionItems = viewModel.deceases.map { it.name }.toTypedArray()
        val conditionIds = viewModel.deceases.map { it.deceaseId }.toTypedArray()
        val checkedItem = intArrayOf(-1)
        for (i in conditionIds.indices) {
            if (viewModel.deceaseId == conditionIds[i]) {
                checkedItem[0] = i
            }
        }
        builder.setSingleChoiceItems(conditionItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            viewModel.deceaseId = conditionIds[which]
            binding.deceaseET.setText(conditionItems[which])
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showSelectPatientDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Пациент:")

        val conditionItems = viewModel.patients.map { "${it.surname} ${it.name} ${it.fatherName}" }.toTypedArray()
        val conditionIds = viewModel.patients.map { it.patientId }.toTypedArray()
        val checkedItem = intArrayOf(-1)
        for (i in conditionIds.indices) {
            if (viewModel.patientId == conditionIds[i]) {
                checkedItem[0] = i
            }
        }
        builder.setSingleChoiceItems(conditionItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            viewModel.patientId = conditionIds[which]
            binding.patientET.setText(conditionItems[which])
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showSelectDoctorDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Доктор:")

        val conditionItems = viewModel.doctors.map { "${it.surname} ${it.name} ${it.fatherName}" }.toTypedArray()
        val conditionIds = viewModel.doctors.map { it.doctorId }.toTypedArray()
        val checkedItem = intArrayOf(-1)
        for (i in conditionIds.indices) {
            if (viewModel.doctorId == conditionIds[i]) {
                checkedItem[0] = i
            }
        }
        builder.setSingleChoiceItems(conditionItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            viewModel.doctorId = conditionIds[which]
            binding.doctorET.setText(conditionItems[which])
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showPickStartDateDialog() {
        val action = AddDiagnosisFragmentDirections.actionGlobalDatePickerFragment()
        findNavController().navigate(action)
    }
}