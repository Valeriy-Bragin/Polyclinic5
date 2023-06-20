package com.meriniguan.polyclinic.ui.screens.addpatients

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.meriniguan.polyclinic.MainActivity
import com.meriniguan.polyclinic.R
import com.meriniguan.polyclinic.databinding.FragmentAddPatientBinding
import com.meriniguan.polyclinic.utils.getDateString
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AddPatientsFragment : Fragment(R.layout.fragment_add_patient) {

    private lateinit var binding: FragmentAddPatientBinding
    private val viewModel by viewModels<AddPatientViewModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddPatientBinding.bind(view)
        (requireActivity() as MainActivity).supportActionBar?.title = "Добавить пациента"
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.apply {
            nameET.setText(viewModel.name)
            fatherNameET.setText(viewModel.fatherName)
            surnameET.setText(viewModel.surname)

            nameET.addTextChangedListener {
                viewModel.name = it.toString()
            }
            fatherNameET.addTextChangedListener {
                viewModel.fatherName = it.toString()
            }
            surnameET.addTextChangedListener {
                viewModel.surname = it.toString()
            }
            if (viewModel.birthDate != 0.toLong()) {
                birthDateET.setText(getDateString(viewModel.birthDate))
            }
            birthDateET.setOnTouchListener { v, event ->
                try {
                    if (event!!.action == MotionEvent.ACTION_DOWN) {
                        showPickBirthDateDialog()
                    }
                } catch (_: Exception) { }
                true
            }
            conditionET.setOnTouchListener { v, event ->
                try {
                    if (event!!.action == MotionEvent.ACTION_DOWN) {
                        showSelectConditionDialog()
                    }
                } catch (_: Exception) { }
                true
            }
            socialStatusET.setOnTouchListener { v, event ->
                try {
                    if (event!!.action == MotionEvent.ACTION_DOWN) {
                        showSelectSocialStatusDialog()
                    }
                } catch (_: Exception) { }
                true
            }
            doneFab.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        setFragmentResultListener("dateReqK") { _, bundle ->
            val milliseconds = bundle.getLong("dateResK")
            viewModel.birthDate = milliseconds
            binding.birthDateET.setText(getDateString(milliseconds))
        }

        viewModel.updateConditionEvent.observe(viewLifecycleOwner) {
            if (viewModel.condition != null) {
                binding.conditionET.setText(viewModel.condition!!.name)
            }
        }
        viewModel.updateSocialStatusEvent.observe(viewLifecycleOwner) {
            if (viewModel.socialStatus != null) {
                binding.socialStatusET.setText(viewModel.socialStatus!!.name)
            }
        }
    }

    private fun showPickBirthDateDialog() {
        val action = AddPatientsFragmentDirections.actionGlobalDatePickerFragment()
        findNavController().navigate(action)
    }

    private fun showSelectConditionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Состояние:")

        val conditionItems = viewModel.getConditions2().map { it.name }.toTypedArray()
        val conditionIds = viewModel.getConditions2().map { it.conditionId }.toTypedArray()
        val checkedItem = intArrayOf(-1)
        for (i in conditionIds.indices) {
            if (viewModel.conditionId == conditionIds[i]) {
                checkedItem[0] = i
            }
        }
        builder.setSingleChoiceItems(conditionItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            viewModel.conditionId = conditionIds[which]
            binding.conditionET.setText(conditionItems[which])
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showSelectSocialStatusDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Социальный статус:")

        val conditionItems = viewModel.socialStatuses.map { it.name }.toTypedArray()
        val conditionIds = viewModel.socialStatuses.map { it.socialStatusId }.toTypedArray()
        val checkedItem = intArrayOf(-1)
        for (i in conditionIds.indices) {
            if (viewModel.socialStateId == conditionIds[i]) {
                checkedItem[0] = i
            }
        }
        builder.setSingleChoiceItems(conditionItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            viewModel.socialStateId = conditionIds[which]
            binding.socialStatusET.setText(conditionItems[which])
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}