package com.meriniguan.polyclinic.ui.screens.diagnosis

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.meriniguan.polyclinic.model.AppRepository
import com.meriniguan.polyclinic.model.diagnosis.room.Diagnosis
import com.meriniguan.polyclinic.model.diagnosis.room.utils.Decease
import com.meriniguan.polyclinic.model.doctor.room.utils.Niche
import com.meriniguan.polyclinic.model.patient.room.Patient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiagnosisViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    var deceases: List<Decease> = listOf()
    var selectedDeceaseId = MutableLiveData<Long?>()

    var niches: List<Niche> = listOf()
    var selectedNicheId = MutableLiveData<Long?>()

    init {
        selectedDeceaseId.value = null
        selectedNicheId.value = null
        viewModelScope.launch {
            repository.getDeceases().collectLatest {
                deceases = it
            }
        }
        viewModelScope.launch {
            repository.getNiches().collectLatest {
                niches = it
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val diagnoses = combine(selectedDeceaseId.asFlow(), selectedNicheId.asFlow()) { deceaseId, nicheId ->
        Pair<Long?, Long?>(deceaseId, nicheId)
    }.flatMapLatest { (deceaseId, nicheId) ->
        repository.getDiagnoses(deceaseId, nicheId)
    }

    fun getDeceases2(): List<Decease> {
        return deceases
    }

    fun removeDeceaseFilter() {
        selectedDeceaseId.value = null
    }

    fun getNiches2(): List<Niche> {
        return niches
    }

    fun removeNichesFilter() {
        selectedNicheId.value = null
    }

    suspend fun getDiagnosis(id: Long): Diagnosis {
        return repository.getDiagnosis(id)
    }
}