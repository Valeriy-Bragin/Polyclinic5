package com.meriniguan.polyclinic.model.diagnosis

data class DiagnosisAndPatientListItem(
    val decease: String = "",
    val patName: String = "",
    val patFatherName: String = "",
    val patSurname: String = "",
    val patSocialStatus: String = "",
    val patCondition: String = "",
    val patId: Long = 0,
    val isAmbTreatmentNeeded: Boolean = false,
    val isOnRecord: Boolean = false,
    val disabilityPeriodMonths: Int = 0,
    val treatmentStartDate: Long = System.currentTimeMillis(),
    val diagnosisId: Long = 0
) {
    override fun toString(): String {
        return "$decease~$diagnosisId - $patName~$patId"
    }
}