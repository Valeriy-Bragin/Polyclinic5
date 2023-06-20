package com.meriniguan.polyclinic.ui.screens.pateients

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.meriniguan.polyclinic.model.AppRepository
import com.meriniguan.polyclinic.model.diagnosis.room.utils.Decease
import com.meriniguan.polyclinic.model.patient.room.Patient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientsViewModel @Inject constructor(
    private val patientsRepository: AppRepository
) : ViewModel() {

    var deceases: List<Decease> = listOf()
    var selectedDeceaseIds = MutableLiveData<List<Long>>()

    init {
        selectedDeceaseIds.value = emptyList()
        viewModelScope.launch {
            patientsRepository.getDeceases().collectLatest {
                deceases = it
            }
        }
    }

    fun getDeceases2(): List<Decease> {
        return deceases
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    var patients = selectedDeceaseIds.asFlow().flatMapLatest {
        patientsRepository.getPatients(it)
    }

    suspend fun getPatient(id: Long): Patient {
        return patientsRepository.getPatient(id)
    }
}