package com.meriniguan.polyclinic.model.diagnosis.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "diagnoses")
data class Diagnosis(
    val diDeceaseId: Long = 0,
    val diDoctorId: Long = 0,
    val diPatientId : Long = 0,
    val isAmbTreatmentNeeded: Boolean = false,
    val isOnRecord: Boolean = false,
    val disabilityPeriodMonths: Int = 0,
    val treatmentStartDate: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val diagnosisId: Long = 0
) : Parcelable {
    override fun toString(): String {
        return "diagId~$diagnosisId"
    }
}