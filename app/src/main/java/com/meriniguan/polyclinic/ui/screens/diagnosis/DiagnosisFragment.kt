package com.meriniguan.polyclinic.ui.screens.diagnosis

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.meriniguan.polyclinic.R
import com.meriniguan.polyclinic.databinding.FragmentDiagnosisBinding
import com.meriniguan.polyclinic.model.diagnosis.DiagnosisListItem
import com.meriniguan.polyclinic.ui.screens.pateients.PatientsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiagnosisFragment : Fragment(R.layout.fragment_diagnosis), DiagnosesAdapter.OnItemClickListener {

    private lateinit var binding: FragmentDiagnosisBinding
    private val viewModel: DiagnosisViewModel by viewModels()

    private lateinit var diagnosesAdapter: DiagnosesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDiagnosisBinding.bind(view)
        diagnosesAdapter = DiagnosesAdapter(requireContext(), this)

        initPatientsList()

        binding.addDiagnosisFab.setOnClickListener {
            val action = DiagnosisFragmentDirections.actionDiagnosisFragmentToAddDiagnosisFragment()
            findNavController().navigate(action)
        }

        setUpMenu()
    }
    private fun initPatientsList() {
        binding.diagnosesRecyclerView.apply {
            adapter = diagnosesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        observeDiagnoses()
    }

    private fun observeDiagnoses() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.diagnoses.collectLatest {
            diagnosesAdapter.submitList(it)
        }
    }

    override fun onItemClick(diagnosis: DiagnosisListItem) {
        viewLifecycleOwner.lifecycleScope.launch {
            Log.i("!@#didi", diagnosis.toString() + " id====" + diagnosis.diagnosisId.toString())
            val d = viewModel.getDiagnosis(diagnosis.diagnosisId)
            val action = DiagnosisFragmentDirections.actionDiagnosisFragmentToAddDiagnosisFragment(diagnosis = d)
            findNavController().navigate(action)
        }
    }

    private fun setUpMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.diagnoses_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.filterDecease -> {
                        showDeceasesDialog()
                        true
                    }
                    R.id.filterNiche -> {
                        showNichesDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showDeceasesDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Выберите болезнь:")

        val deceaseItems = viewModel.getDeceases2().map { it.name }.toTypedArray()
        val deceaseIds = viewModel.getDeceases2().map { it.deceaseId }.toTypedArray()
        val checkedItem = intArrayOf(-1)
        builder.setSingleChoiceItems(deceaseItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            viewModel.selectedDeceaseId.value = deceaseIds[which]
            dialog.dismiss()
        }
        builder.setNegativeButton("Отмена") { _, _ -> }
        builder.setNeutralButton("Убрать фильтр") { _, _ ->
            viewModel.removeDeceaseFilter()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showNichesDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Выберите специальность:")

        val nicheItems = viewModel.getNiches2().map { it.name }.toTypedArray()
        val nicheIds = viewModel.getNiches2().map { it.nicheId }.toTypedArray()
        val checkedItem = intArrayOf(-1)
        builder.setSingleChoiceItems(nicheItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            viewModel.selectedNicheId.value = nicheIds[which]
            dialog.dismiss()
        }
        builder.setNegativeButton("Отмена") { _, _ -> }
        builder.setNeutralButton("Убрать фильтр") { _, _ ->
            viewModel.removeNichesFilter()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}