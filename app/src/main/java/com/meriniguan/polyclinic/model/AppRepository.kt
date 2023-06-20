package com.meriniguan.polyclinic.model

import com.meriniguan.polyclinic.model.diagnosis.DiagnosisAndDoctorListItem
import com.meriniguan.polyclinic.model.diagnosis.DiagnosisAndPatientListItem
import com.meriniguan.polyclinic.model.diagnosis.DiagnosisListItem
import com.meriniguan.polyclinic.model.diagnosis.room.Diagnosis
import com.meriniguan.polyclinic.model.diagnosis.room.utils.Decease
import com.meriniguan.polyclinic.model.doctor.DoctorListItem
import com.meriniguan.polyclinic.model.doctor.room.Doctor
import com.meriniguan.polyclinic.model.doctor.room.utils.Niche
import com.meriniguan.polyclinic.model.doctor.room.utils.Qualification
import com.meriniguan.polyclinic.model.patient.PatientListItem
import com.meriniguan.polyclinic.model.patient.room.Patient
import com.meriniguan.polyclinic.model.patient.room.utils.Condition
import com.meriniguan.polyclinic.model.patient.room.utils.SocialStatus
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    fun getPatients(deceaseIds: List<Long>): Flow<List<PatientListItem>>

    fun getPatients2(): Flow<List<Patient>>

    suspend fun getPatient(id: Long): Patient

    suspend fun addPatient(patient: Patient)

    suspend fun removePatient(patient: Patient)

    suspend fun updatePatient(patient: Patient)


    fun getSocialStatuses(): Flow<List<SocialStatus>>

    suspend fun getSocialStatus(id: Long): SocialStatus

    suspend fun addSocialStatus(socialStatus: SocialStatus)


    fun getConditions(): Flow<List<Condition>>

    suspend fun getCondition(id: Long): Condition

    suspend fun addCondition(condition: Condition)


    fun getDoctors(patientSocialStatusId: Long? = null): Flow<List<DoctorListItem>>

    fun getDoctors2(): Flow<List<Doctor>>

    suspend fun getDoctor(id: Long): Doctor

    suspend fun addDoctor(doctor: Doctor)

    suspend fun removeDoctor(doctor: Doctor)

    suspend fun updateDoctor(doctor: Doctor)


    fun getNiches(): Flow<List<Niche>>

    suspend fun addNiche(niche: Niche)


    fun getQualifications(): Flow<List<Qualification>>

    suspend fun addQualification(qualification: Qualification)


    fun getDiagnoses(deceaseId: Long? = null, nicheId: Long? = null): Flow<List<DiagnosisListItem>>

    fun getDiagnosesWithDoctors(patientId: Long): Flow<List<DiagnosisAndDoctorListItem>>

    fun getDiagnosesWithPatients(doctorId: Long): Flow<List<DiagnosisAndPatientListItem>>

    suspend fun addDiagnosis(diagnosis: Diagnosis)

    suspend fun getDiagnosis(id: Long): Diagnosis


    fun getDeceases(): Flow<List<Decease>>

    suspend fun getDecease(id: Long): Decease

    suspend fun addDecease(decease: Decease)

    suspend fun updateDiagnosis(diagnosis: Diagnosis)
}