package com.meriniguan.polyclinic.model.patient

data class PatientListItem(
    val name: String,
    val fatherName: String = "",
    val surname: String = "",
    val socialStatus: String = "",
    val condition: String = "",
    val birthDate: Long = 0,
    val id: Long = 0
) {
    override fun toString(): String {
        return "$name~$socialStatus~$condition~$id"
    }
}