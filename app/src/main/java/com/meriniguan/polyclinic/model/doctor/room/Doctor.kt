package com.meriniguan.polyclinic.model.doctor.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctors")
data class Doctor(
    val name: String,
    val fatherName: String = "",
    val surname: String = "",
    val dNicheId: Long = 0,
    val dQualificationId: Long = 0,
    @PrimaryKey(autoGenerate = true) val doctorId: Long = 0
) {
    override fun toString(): String {
        return "$name~$doctorId"
    }
}