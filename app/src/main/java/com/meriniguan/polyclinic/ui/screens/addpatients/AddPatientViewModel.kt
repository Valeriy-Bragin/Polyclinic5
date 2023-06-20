package com.meriniguan.polyclinic.ui.screens.addpatients

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meriniguan.polyclinic.model.AppRepository
import com.meriniguan.polyclinic.model.diagnosis.room.utils.Decease
import com.meriniguan.polyclinic.model.doctor.room.utils.Niche
import com.meriniguan.polyclinic.model.patient.room.Patient
import com.meriniguan.polyclinic.model.patient.room.utils.Condition
import com.meriniguan.polyclinic.model.patient.room.utils.SocialStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPatientViewModel @Inject constructor(
    private val repository: AppRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    val patient = state.get<Patient>("patient")

    val updateConditionEvent = MutableLiveData<Unit>()
    val updateSocialStatusEvent = MutableLiveData<Unit>()

    var name = state.get<String>("patName") ?: patient?.name ?: ""
        set(value) {
            field = value
            state["patName"] = value
        }

    var fatherName = state.get<String>("patFatherName") ?: patient?.fatherName ?: ""
        set(value) {
            field = value
            state["patFatherName"] = value
        }

    var surname = state.get<String>("patSurname") ?: patient?.surname ?: ""
        set(value) {
            field = value
            state["patSurname"] = value
        }

    var birthDate = state.get<Long>("birthDate") ?: patient?.birthDate ?: 0
        set(value) {
            field = value
            state["birthDate"] = value
        }

    var conditionId = state.get<Long>("conditionId") ?: patient?.pConditionId ?: -1
        set(value) {
            field = value
            state["conditionId"] = value
        }
    var condition: Condition? = null

    var socialStateId = state.get<Long>("socialStateId") ?: patient?.pSocialStatusId ?: -1
        set(value) {
            field = value
            state["socialStatusId"] = value
        }
    var socialStatus: SocialStatus? = null


    var conditions: List<Condition> = listOf()
    //var selectedConditionId = MutableLiveData<Long?>()

    var socialStatuses: List<SocialStatus> = listOf()
    //var selectedStatusId = MutableLiveData<Long?>()

    init {
        viewModelScope.launch {
            repository.getSocialStatuses().collectLatest {
                socialStatuses = it
            }
        }
        viewModelScope.launch {
            repository.getConditions().collectLatest {
                conditions = it
            }
        }
        viewModelScope.launch {
            if (!isAdding()) {
                condition = repository.getCondition(conditionId)
                updateConditionEvent.postValue(Unit)
            }
        }
        viewModelScope.launch {
            if (!isAdding()) {
                socialStatus = repository.getSocialStatus(socialStateId)
                updateSocialStatusEvent.postValue(Unit)
            }
        }
    }

    fun getConditions2(): List<Condition> {
        Log.i("!@#blyaaaaa", conditions.toString())
        return conditions
    }

    fun onSaveClick() {
        if (isAdding()) {
            addPatient()
        } else {
            editPatient()
        }
    }

    private fun addPatient() = viewModelScope.launch {
        repository.addPatient(
            Patient(
            name = name,
            fatherName = fatherName,
            surname = surname,
            pSocialStatusId = socialStateId,
            pConditionId = conditionId,
            birthDate = birthDate
        )
        )
    }

    private fun editPatient() = viewModelScope.launch {
        repository.updatePatient(patient!!.copy(
            name = name,
            fatherName = fatherName,
            surname = surname,
            pSocialStatusId = socialStateId,
            pConditionId = conditionId,
            birthDate = birthDate
        ))
    }

    fun isAdding(): Boolean {
        return patient == null
    }

}