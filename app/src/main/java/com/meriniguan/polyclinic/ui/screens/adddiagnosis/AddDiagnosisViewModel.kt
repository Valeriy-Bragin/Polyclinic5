package com.meriniguan.polyclinic.ui.screens.adddiagnosis

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meriniguan.polyclinic.model.AppRepository
import com.meriniguan.polyclinic.model.diagnosis.room.Diagnosis
import com.meriniguan.polyclinic.model.diagnosis.room.utils.Decease
import com.meriniguan.polyclinic.model.doctor.room.Doctor
import com.meriniguan.polyclinic.model.patient.room.Patient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDiagnosisViewModel @Inject constructor(
    private val repository: AppRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    val diagnosis = state.get<Diagnosis>("diagnosis")

    val updateDeceaseEvent = MutableLiveData<Unit>()
    val updatePatientEvent = MutableLiveData<Unit>()
    val updateDoctorEvent = MutableLiveData<Unit>()

    var deceaseId = state.get<Long>("deceaseId") ?: diagnosis?.diDeceaseId ?: -1
        set(value) {
            field = value
            state["deceaseId"] = value
        }

    var patientId = state.get<Long>("patientId") ?: diagnosis?.diPatientId ?: -1
        set(value) {
            field = value
            state["patientId"] = value
        }

    var doctorId = state.get<Long>("doctorId") ?: diagnosis?.diDoctorId ?: -1
        set(value) {
            field = value
            state["doctorId"] = value
        }

    var ambNeeded = state.get<Boolean>("ambNeeded") ?: diagnosis?.isAmbTreatmentNeeded ?: false
        set(value) {
            field = value
            state["ambNeeded"] = value
        }

    var onRecord = state.get<Boolean>("onRecord") ?: diagnosis?.isOnRecord ?: false
        set(value) {
            field = value
            state["onRecord"] = value
        }

    var period = state.get<Int>("period") ?: diagnosis?.disabilityPeriodMonths ?: -1
        set(value) {
            field = value
            state["period"] = value
        }

    var start = state.get<Long>("start") ?: diagnosis?.treatmentStartDate ?: 0
        set(value) {
            field = value
            state["start"] = value
        }

    var decease: Decease? = null
    var patient: Patient? = null
    var doctor: Doctor? = null

    var deceases: List<Decease> = listOf()
    var patients: List<Patient> = listOf()
    var doctors: List<Doctor> = listOf()

    init {
        viewModelScope.launch {
            if (!isAdding()) {
                decease = repository.getDecease(deceaseId)
                updateDeceaseEvent.postValue(Unit)
            }
        }
        viewModelScope.launch {
            if (!isAdding()) {
                patient = repository.getPatient(patientId)
                updatePatientEvent.postValue(Unit)
            }
        }
        viewModelScope.launch {
            if (!isAdding()) {
                doctor = repository.getDoctor(doctorId)
                updateDoctorEvent.postValue(Unit)
            }
        }
        viewModelScope.launch {
            repository.getDeceases().collectLatest {
                deceases = it
            }
        }
        viewModelScope.launch {
            repository.getPatients2().collectLatest {
                patients = it
            }
        }
        viewModelScope.launch {
            repository.getDoctors2().collectLatest {
                doctors = it
            }
        }
    }

    fun onSaveClick() {
        if (isAdding()) {
            addDiagnosis()
        } else {
            editDiagnosis()
        }
    }

    private fun addDiagnosis() = viewModelScope.launch {
        repository.addDiagnosis(
            Diagnosis(
                deceaseId,
                doctorId,
                patientId,
                ambNeeded,
                onRecord,
                period,
                start
            )
        )
    }

    private fun editDiagnosis() = viewModelScope.launch {
        repository.updateDiagnosis(
            diagnosis!!.copy(
                deceaseId,
                doctorId,
                patientId,
                ambNeeded,
                onRecord,
                period,
                start
            )
        )
    }


    fun isAdding(): Boolean = diagnosis == null
}