package com.meriniguan.polyclinic.model.patient.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.meriniguan.polyclinic.model.patient.room.utils.PatientAndCondition
import com.meriniguan.polyclinic.model.patient.room.utils.PatientAndSocialStatus
import com.meriniguan.polyclinic.model.patient.room.utils.PatientWithDiagnoses
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientsDao {

    @Insert
    suspend fun addPatient(patient: Patient)

    @Update
    suspend fun updatePatient(patient: Patient)

    @Delete
    suspend fun deletePatient(patient: Patient)

    @Transaction
    @Query("SELECT * FROM patients")
    fun getPatientsAndSocialStatuses(): Flow<List<PatientAndSocialStatus>>

    @Transaction
    @Query("SELECT * FROM patients")
    fun getPatientsAndConditions(): Flow<List<PatientAndCondition>>

    @Transaction
    @Query("SELECT * FROM patients")
    fun getPatientsWithDiagnoses(): Flow<List<PatientWithDiagnoses>>

    @Query("SELECT * FROM patients WHERE patientId ==:id")
    suspend fun getPatient(id: Long): Patient

    @Query("SELECT * FROM patients")
    fun getPatients(): Flow<List<Patient>>
}