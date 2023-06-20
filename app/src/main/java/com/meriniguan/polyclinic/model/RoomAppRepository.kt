package com.meriniguan.polyclinic.model

import com.meriniguan.polyclinic.model.diagnosis.DiagnosisAndDoctorListItem
import com.meriniguan.polyclinic.model.diagnosis.DiagnosisAndPatientListItem
import com.meriniguan.polyclinic.model.diagnosis.DiagnosisListItem
import com.meriniguan.polyclinic.model.diagnosis.room.DiagnosesDao
import com.meriniguan.polyclinic.model.diagnosis.room.Diagnosis
import com.meriniguan.polyclinic.model.diagnosis.room.utils.Decease
import com.meriniguan.polyclinic.model.diagnosis.room.utils.DeceasesDao
import com.meriniguan.polyclinic.model.doctor.DoctorListItem
import com.meriniguan.polyclinic.model.doctor.room.Doctor
import com.meriniguan.polyclinic.model.doctor.room.DoctorsDao
import com.meriniguan.polyclinic.model.doctor.room.utils.Niche
import com.meriniguan.polyclinic.model.doctor.room.utils.NichesDao
import com.meriniguan.polyclinic.model.doctor.room.utils.Qualification
import com.meriniguan.polyclinic.model.doctor.room.utils.QualificationsDao
import com.meriniguan.polyclinic.model.patient.PatientListItem
import com.meriniguan.polyclinic.model.patient.room.Patient
import com.meriniguan.polyclinic.model.patient.room.PatientsDao
import com.meriniguan.polyclinic.model.patient.room.utils.Condition
import com.meriniguan.polyclinic.model.patient.room.utils.ConditionsDao
import com.meriniguan.polyclinic.model.patient.room.utils.SocialStatus
import com.meriniguan.polyclinic.model.patient.room.utils.SocialStatusesDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomAppRepository @Inject constructor(
    private val patientsDao: PatientsDao,
    private val socialStatusesDao: SocialStatusesDao,
    private val conditionsDao: ConditionsDao,
    private val doctorsDao: DoctorsDao,
    private val nichesDao: NichesDao,
    private val qualificationsDao: QualificationsDao,
    private val diagnosesDao: DiagnosesDao,
    private val deceaseDao: DeceasesDao,
    private val ioDispatcher: CoroutineDispatcher
) : AppRepository {

    /*override fun getPatients(): Flow<List<PatientListItem>> {
        return combine(patientsDao.getPatientsAndSocialStatuses(), patientsDao.getPatientsAndConditions()) { patientsWithSocialStatuses, patientsWithConditions ->
            val list = mutableListOf<PatientListItem>()
            for (i in patientsWithSocialStatuses.indices) {
                val patient = patientsWithSocialStatuses[i].patient
                val socialStatus = patientsWithSocialStatuses[i].socialStatus.name
                val condition = patientsWithConditions[i].condition.name
                list.add(PatientListItem(patient.name, patient.fatherName, patient.surname, socialStatus, condition, patient.birthDate, patient.patientId))
            }
            list
        }
    }*/

    override fun getPatients(deceaseIds: List<Long>): Flow<List<PatientListItem>> {
        return patientsDao.getPatientsWithDiagnoses().map {
            val resultList = mutableListOf<PatientListItem>()
            for (i in it) {
                val patient = i.patient
                val diagnoses = i.diagnoses
                if (containAllDeceases(deceaseIds, diagnoses)) {
                    resultList.add(
                        PatientListItem(
                        name = patient.name,
                        fatherName = patient.fatherName,
                        surname = patient.surname,
                        socialStatus = socialStatusesDao.getSocialStatus(patient.pSocialStatusId).name,
                        condition = conditionsDao.getCondition(patient.pConditionId).name,
                        birthDate = patient.birthDate,
                        id = patient.patientId
                    )
                    )
                }
            }
            resultList
        }
    }

    override suspend fun getPatient(id: Long): Patient {
        return patientsDao.getPatient(id)
    }

    private fun containAllDeceases(deceaseIds: List<Long>, diagnoses: List<Diagnosis>): Boolean {
        val ids = deceaseIds.toMutableList()
        for (diagnosis in diagnoses) {
            if (ids.contains(diagnosis.diDeceaseId)) {
                ids.remove(diagnosis.diDeceaseId)
            }
        }
        if (ids.isEmpty()) {
            return true
        }
        return false
    }


    override suspend fun addPatient(patient: Patient) = withContext(ioDispatcher) {
        patientsDao.addPatient(patient)
    }

    override suspend fun removePatient(patient: Patient) = withContext(ioDispatcher) {
        patientsDao.deletePatient(patient)
    }

    override suspend fun updatePatient(patient: Patient) = withContext(ioDispatcher) {
        patientsDao.updatePatient(patient)
    }

    override suspend fun addSocialStatus(socialStatus: SocialStatus) = withContext(ioDispatcher) {
        socialStatusesDao.addSocialStatus(socialStatus)
    }

    override fun getSocialStatuses(): Flow<List<SocialStatus>> {
        return socialStatusesDao.getSocialStatuses()
    }

    override suspend fun getSocialStatus(id: Long): SocialStatus {
        return socialStatusesDao.getSocialStatus(id)
    }

    override suspend fun addCondition(condition: Condition) = withContext(ioDispatcher) {
        conditionsDao.addCondition(condition)
    }

    override fun getConditions(): Flow<List<Condition>> {
        return conditionsDao.getConditions()
    }

    override suspend fun getCondition(id: Long): Condition {
        return conditionsDao.getCondition(id)
    }

    /*override fun getDoctors(): Flow<List<DoctorListItem>> {
        return combine(doctorsDao.getDoctorsAndNiches(), doctorsDao.getDoctorsAndQualifications()) { doctorsAndNiches, doctorsAndQualifictions ->
            val list = mutableListOf<DoctorListItem>()
            for (i in doctorsAndNiches.indices) {
                val doctor = doctorsAndNiches[i].doctor
                val niche = doctorsAndNiches[i].niche.name
                val qualification = doctorsAndQualifictions[i].qualification.name
                list.add(DoctorListItem(doctor.name, doctor.fatherName, doctor.surname, niche, qualification))
            }
            list
        }
    }*/

    override fun getDoctors(patientSocialStatusId: Long?): Flow<List<DoctorListItem>> {
        return doctorsDao.getDoctorsWithDiagnoses().map {
            val resultList = mutableListOf<DoctorListItem>()
            for (i in it) {
                val doctor = i.doctor
                val diagnoses = i.diagnoses
                if (patientSocialStatusId == null || containOnlyWithSuchSocialStatus(patientSocialStatusId, diagnoses)) {
                    resultList.add(
                        DoctorListItem(
                            name = doctor.name,
                            fatherName = doctor.fatherName,
                            surname = doctor.surname,
                            niche = nichesDao.getNiche(doctor.dNicheId).name,
                            qualification = qualificationsDao.getQualification(doctor.dQualificationId).name,
                            id = doctor.doctorId
                        )
                    )
                }
            }
            resultList
        }
    }

    override suspend fun getDoctor(id: Long): Doctor {
        return doctorsDao.getDoctor(id)
    }

    override fun getDiagnoses(deceaseId: Long?, nicheId: Long?): Flow<List<DiagnosisListItem>> {
        return diagnosesDao.getDiagnoses().map {
            val resultList = mutableListOf<DiagnosisListItem>()
            for (diagnosis in it) {
                val patient = patientsDao.getPatient(diagnosis.diPatientId)
                val doctor = doctorsDao.getDoctor(diagnosis.diDoctorId)
                if ((deceaseId == null || diagnosis.diDeceaseId == deceaseId) && (nicheId == null || doctor.dNicheId == nicheId)) {
                    resultList.add(
                        DiagnosisListItem(
                            decease = deceaseDao.getDecease(diagnosis.diDeceaseId).name,
                            docName = doctor.name,
                            docFatherName = doctor.fatherName,
                            docSurname = doctor.surname,
                            patName = patient.name,
                            patFatherName = patient.fatherName,
                            patSurname = patient.surname,
                            isAmbTreatmentNeeded = diagnosis.isAmbTreatmentNeeded,
                            isOnRecord = diagnosis.isOnRecord,
                            disabilityPeriodMonths = diagnosis.disabilityPeriodMonths,
                            treatmentStartDate = diagnosis.treatmentStartDate,
                            diagnosisId = diagnosis.diagnosisId
                        )
                    )
                }
            }
            resultList
        }
    }

    private suspend fun containOnlyWithSuchSocialStatus(patientSocialStatusId: Long, diagnoses: List<Diagnosis>): Boolean {
        if (diagnoses.isEmpty()) return false
        for (diagnosis in diagnoses) {
            val currentPatientSocialStatusId = patientsDao.getPatient(diagnosis.diPatientId).pSocialStatusId
            if (currentPatientSocialStatusId != patientSocialStatusId) {
                return false
            }
        }
        return true
    }

    override suspend fun addDoctor(doctor: Doctor) = withContext(ioDispatcher) {
        doctorsDao.addDoctor(doctor)
    }

    override suspend fun removeDoctor(doctor: Doctor) = withContext(ioDispatcher) {
        doctorsDao.deleteDoctor(doctor)
    }

    override suspend fun updateDoctor(doctor: Doctor) = withContext(ioDispatcher) {
        doctorsDao.updateDoctor(doctor)
    }

    override suspend fun addNiche(niche: Niche) = withContext(ioDispatcher) {
        nichesDao.addNiche(niche)
    }

    override fun getNiches(): Flow<List<Niche>> {
        return nichesDao.getNiches()
    }

    override suspend fun addQualification(qualification: Qualification) = withContext(ioDispatcher) {
        qualificationsDao.addQualification(qualification)
    }

    override fun getQualifications(): Flow<List<Qualification>> {
        return qualificationsDao.getQualifications()
    }

    override suspend fun addDiagnosis(diagnosis: Diagnosis) = withContext(ioDispatcher) {
        diagnosesDao.addDiagnosis(diagnosis)
    }

    override suspend fun getDiagnosis(id: Long): Diagnosis {
        return diagnosesDao.getDiagnosis(id)
    }

    override fun getDiagnosesWithDoctors(patientId: Long): Flow<List<DiagnosisAndDoctorListItem>> {
        return diagnosesDao.getDiagnosesAndDeceases().map { diagnosesAndDeceases ->
            val list = mutableListOf<DiagnosisAndDoctorListItem>()
            for (diagnosisAndDecease in diagnosesAndDeceases) {
                if (diagnosisAndDecease.diagnosis.diPatientId == patientId) {
                    val diagnosis = diagnosisAndDecease.diagnosis
                    val decease = diagnosisAndDecease.decease.name
                    val doctor = doctorsDao.getDoctor(diagnosis.diDoctorId)
                    val niche = nichesDao.getNiche(doctor.dNicheId).name
                    val qualification = qualificationsDao.getQualification(doctor.dQualificationId).name
                    list.add(
                        DiagnosisAndDoctorListItem(
                            decease,
                            doctor.name,
                            doctor.fatherName,
                            doctor.surname,
                            niche,
                            qualification,
                            doctor.doctorId,
                            diagnosis.isAmbTreatmentNeeded,
                            diagnosis.isOnRecord,
                            diagnosis.disabilityPeriodMonths,
                            diagnosis.treatmentStartDate,
                            diagnosis.diagnosisId
                        )
                    )
                }
            }
            list
        }
    }

    override fun getDiagnosesWithPatients(doctorId: Long): Flow<List<DiagnosisAndPatientListItem>> {
        return diagnosesDao.getDiagnosesAndDeceases().map { diagnosesAndDeceases ->
            val list = mutableListOf<DiagnosisAndPatientListItem>()
            for (diagnosisAndDecease in diagnosesAndDeceases) {
                if (diagnosisAndDecease.diagnosis.diDoctorId == doctorId) {
                    val diagnosis = diagnosisAndDecease.diagnosis
                    val decease = diagnosisAndDecease.decease.name
                    val patient = patientsDao.getPatient(diagnosis.diPatientId)
                    val socialStatus = socialStatusesDao.getSocialStatus(patient.pSocialStatusId).name
                    val condition = conditionsDao.getCondition(patient.pConditionId).name
                    list.add(
                        DiagnosisAndPatientListItem(
                            decease,
                            patient.name,
                            patient.fatherName,
                            patient.surname,
                            socialStatus,
                            condition,
                            patient.patientId,
                            diagnosis.isAmbTreatmentNeeded,
                            diagnosis.isOnRecord,
                            diagnosis.disabilityPeriodMonths,
                            diagnosis.treatmentStartDate,
                            diagnosis.diagnosisId
                        )
                    )
                }
            }
            list
        }
    }

    override suspend fun addDecease(decease: Decease) = withContext(ioDispatcher) {
        deceaseDao.addDecease(decease)
    }

    override suspend fun updateDiagnosis(diagnosis: Diagnosis) = withContext(ioDispatcher) {
        diagnosesDao.updateDiagnosis(diagnosis)
    }

    override fun getDeceases(): Flow<List<Decease>> {
        return deceaseDao.getDeceases()
    }

    override suspend fun getDecease(id: Long): Decease {
        return deceaseDao.getDecease(id)
    }

    override fun getPatients2(): Flow<List<Patient>> {
        return patientsDao.getPatients()
    }

    override fun getDoctors2(): Flow<List<Doctor>> {
        return doctorsDao.getDoctors()
    }
}