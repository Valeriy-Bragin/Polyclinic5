package com.meriniguan.polyclinic.model.doctor.room.utils

import androidx.room.Embedded
import androidx.room.Relation
import com.meriniguan.polyclinic.model.diagnosis.room.Diagnosis
import com.meriniguan.polyclinic.model.doctor.room.Doctor

data class DoctorWithDiagnoses(
    @Embedded val doctor: Doctor,
    @Relation(
        parentColumn = "doctorId",
        entityColumn = "diDoctorId"
    )
    val diagnoses: List<Diagnosis>
)