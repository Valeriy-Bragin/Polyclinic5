package com.meriniguan.polyclinic.ui.screens.doctors

import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.meriniguan.polyclinic.R
import com.meriniguan.polyclinic.databinding.FragmentDoctorsBinding
import com.meriniguan.polyclinic.model.doctor.DoctorListItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DoctorsFragment : Fragment(R.layout.fragment_doctors), DoctorsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentDoctorsBinding
    private val viewModel by viewModels<DoctorsViewModel>()

    private lateinit var doctorsAdapter: DoctorsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDoctorsBinding.bind(view)
        doctorsAdapter = DoctorsAdapter(this)

        initDoctorsList()

        setUpMenu()
    }

    private fun initDoctorsList() {
        binding.patientsRecyclerView.apply {
            adapter = doctorsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        observePatients()
    }

    private fun observePatients() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.doctors.collectLatest {
            doctorsAdapter.submitList(it)
        }
    }

    override fun onItemClick(patient: DoctorListItem) {

    }

    private fun setUpMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.doctors_menu, menu)



            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.filterDoctors -> {
                        showPatientSocialStatusesDialog()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showPatientSocialStatusesDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Лечили только:")

        val statusItems = viewModel.getSocialStatuses2().map { it.name }.toTypedArray()
        val statusIds = viewModel.getSocialStatuses2().map { it.socialStatusId }.toTypedArray()
        val checkedItem = intArrayOf(-1)
        for (i in statusIds.indices) {
            if (viewModel.selectedSocialStatusId.value == statusIds[i]) {
                checkedItem[0] = i
            }
        }
        builder.setSingleChoiceItems(statusItems, checkedItem[0]) { dialog, which ->
            checkedItem[0] = which
            viewModel.selectedSocialStatusId.value = statusIds[which]
            dialog.dismiss()
        }
        builder.setNegativeButton("Отмена") { _, _ -> }
        builder.setNeutralButton("Убрать фильтр") { _, _ ->
            viewModel.removeSocialStatusFilter()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}