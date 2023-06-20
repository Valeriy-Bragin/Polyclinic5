package com.meriniguan.polyclinic.model.patient.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "patients")
data class Patient(
    val name: String,
    val fatherName: String = "",
    val surname: String = "",
    val pSocialStatusId: Long = 0,
    val pConditionId: Long = 0,
    val birthDate: Long = 0,
    @PrimaryKey(autoGenerate = true) val patientId: Long = 0
) : Parcelable {
    override fun toString(): String {
        return "$name~$patientId"
    }
}