package com.meriniguan.polyclinic.model.diagnosis

data class DiagnosisAndDoctorListItem(
    val decease: String = "",
    val docName: String = "",
    val docFatherName: String = "",
    val docSurname: String = "",
    val docNiche: String = "",
    val docQualification: String = "",
    val docId: Long = 0,
    val isAmbTreatmentNeeded: Boolean = false,
    val isOnRecord: Boolean = false,
    val disabilityPeriodMonths: Int = 0,
    val treatmentStartDate: Long = System.currentTimeMillis(),
    val diagnosisId: Long = 0
) {
    override fun toString(): String {
        return "$diagnosisId - $docName~$docId"
    }
}