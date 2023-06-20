package com.meriniguan.polyclinic.model.patient.room.utils

import androidx.room.Embedded
import androidx.room.Relation
import com.meriniguan.polyclinic.model.diagnosis.room.Diagnosis
import com.meriniguan.polyclinic.model.patient.room.Patient

data class PatientWithDiagnoses(
    @Embedded val patient: Patient,
    @Relation(
        parentColumn = "patientId",
        entityColumn = "diPatientId"
    )
    val diagnoses: List<Diagnosis>
)