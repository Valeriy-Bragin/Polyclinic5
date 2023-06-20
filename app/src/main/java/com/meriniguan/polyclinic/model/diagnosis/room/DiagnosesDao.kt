package com.meriniguan.polyclinic.model.diagnosis.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.meriniguan.polyclinic.model.diagnosis.room.utils.DiagnosisAndDecease
import com.meriniguan.polyclinic.model.doctor.room.utils.DoctorAndNiche
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosesDao {

    @Insert
    suspend fun addDiagnosis(diagnosis: Diagnosis)

    @Update
    suspend fun updateDiagnosis(diagnosis: Diagnosis)

    @Delete
    suspend fun deleteDiagnosis(diagnosis: Diagnosis)

    @Transaction
    @Query("SELECT * FROM diagnoses")
    fun getDiagnosesAndDeceases(): Flow<List<DiagnosisAndDecease>>

    @Query("SELECT * FROM diagnoses")
    fun getDiagnoses(): Flow<List<Diagnosis>>

    @Query("SELECT * FROM diagnoses WHERE diagnosisId ==:id")
    suspend fun getDiagnosis(id: Long): Diagnosis
}