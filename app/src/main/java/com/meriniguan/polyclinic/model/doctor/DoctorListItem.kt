package com.meriniguan.polyclinic.model.doctor

import androidx.room.PrimaryKey

data class DoctorListItem(
    val name: String,
    val fatherName: String= "",
    val surname: String= "",
    val niche: String= "",
    val qualification: String= "",
    val id: Long = 0
) {
    override fun toString(): String {
        return "$name~$id"
    }
}