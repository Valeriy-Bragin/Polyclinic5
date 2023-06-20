package com.meriniguan.polyclinic.model.diagnosis

data class DiagnosisListItem(
    val decease: String = "",
    val docName: String = "",
    val docFatherName: String = "",
    val docSurname: String = "",
    val patName: String = "",
    val patFatherName: String = "",
    val patSurname: String = "",
    val isAmbTreatmentNeeded: Boolean = false,
    val isOnRecord: Boolean = false,
    val disabilityPeriodMonths: Int = 0,
    val treatmentStartDate: Long = System.currentTimeMillis(),
    val diagnosisId: Long = 0
) {
}