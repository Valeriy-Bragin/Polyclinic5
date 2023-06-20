package com.meriniguan.polyclinic.ui.screens.doctors

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.meriniguan.polyclinic.model.AppRepository
import com.meriniguan.polyclinic.model.patient.room.utils.SocialStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorsViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    var socialStatuses: List<SocialStatus> = listOf()
    var selectedSocialStatusId = MutableLiveData<Long?>()

    init {
        selectedSocialStatusId.value = null
        viewModelScope.launch {
            repository.getSocialStatuses().collectLatest {
                socialStatuses = it
            }
        }
    }

    fun getSocialStatuses2(): List<SocialStatus> {
        return socialStatuses
    }

    fun removeSocialStatusFilter() {
        selectedSocialStatusId.value = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    var doctors = selectedSocialStatusId.asFlow().flatMapLatest {
        repository.getDoctors(it)
    }
}