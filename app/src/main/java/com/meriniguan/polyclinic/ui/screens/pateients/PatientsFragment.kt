package com.meriniguan.polyclinic.ui.screens.pateients

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.meriniguan.polyclinic.MainActivity
import com.meriniguan.polyclinic.R
import com.meriniguan.polyclinic.databinding.FragmentPatientsBinding
import com.meriniguan.polyclinic.model.diagnosis.room.utils.Decease
import com.meriniguan.polyclinic.model.patient.PatientListItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Arrays

@AndroidEntryPoint
class PatientsFragment : Fragment(R.layout.fragment_patients), PatientsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentPatientsBinding
    private val viewModel: PatientsViewModel by viewModels()

    private lateinit var patientsAdapter: PatientsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = "Поликлиника"
        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding = FragmentPatientsBinding.bind(view)
        patientsAdapter = PatientsAdapter(requireContext(), this)

        initPatientsList()

        binding.addPatientFab.setOnClickListener {
            val action = PatientsFragmentDirections.actionPatientsFragmentToAddPatientsFragment()
            findNavController().navigate(action)
        }

        setUpMenu()
    }

    private fun initPatientsList() {
        binding.patientsRecyclerView.apply {
            adapter = patientsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        observePatients()
    }

    private fun observePatients() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.patients.collectLatest {
            patientsAdapter.submitList(it)
        }
    }

    override fun onItemClick(patient: PatientListItem) {
        viewLifecycleOwner.lifecycleScope.launch {
            val p = viewModel.getPatient(patient.id)
            val action = PatientsFragmentDirections.actionPatientsFragmentToAddPatientsFragment(patient = p)
            findNavController().navigate(action)
        }
    }

    private fun setUpMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.patients_menu, menu)



            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.testItem -> {
                        showDeceasesDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showDeceasesDialog() {
        val deceaseItems = viewModel.getDeceases2().map { it.name }.toTypedArray()
        val deceaseIds = viewModel.getDeceases2().map { it.deceaseId }.toTypedArray()
        val checkedItems = BooleanArray(deceaseItems.size)
        val selectedIds = viewModel.selectedDeceaseIds.value!!
        for (i in deceaseIds.indices) {
            if (selectedIds.contains(deceaseIds[i])) {
                checkedItems[i] = true
            }
        }

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select deceases")
        builder.setMultiChoiceItems(deceaseItems, checkedItems) { dialog, which, isChecked ->
            checkedItems[which] = isChecked
        }
        builder.setCancelable(false)
        builder.setPositiveButton("Done") { dialog, which ->
            val selectedDeceaseIds = mutableListOf<Long>()
            for (i in checkedItems.indices) {
                if (checkedItems[i]) {
                    //Log.i("!@#voitenko", "${deceaseItems[i]}~${deceaseIds[i]}")
                    selectedDeceaseIds.add(deceaseIds[i])
                }
            }
            viewModel.selectedDeceaseIds.value = selectedDeceaseIds
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.setNeutralButton("Select all") { dialog, which ->
            Arrays.fill(checkedItems, true)
            viewModel.selectedDeceaseIds.value = deceaseIds.toList()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}