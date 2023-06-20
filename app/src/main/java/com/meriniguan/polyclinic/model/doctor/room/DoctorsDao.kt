package com.meriniguan.polyclinic.model.doctor.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.meriniguan.polyclinic.model.doctor.room.Doctor
import com.meriniguan.polyclinic.model.doctor.room.utils.DoctorAndNiche
import com.meriniguan.polyclinic.model.doctor.room.utils.DoctorAndQualification
import com.meriniguan.polyclinic.model.doctor.room.utils.DoctorWithDiagnoses
import com.meriniguan.polyclinic.model.patient.room.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorsDao {

    @Insert
    suspend fun addDoctor(doctor: Doctor)

    @Update
    suspend fun updateDoctor(doctor: Doctor)

    @Delete
    suspend fun deleteDoctor(doctor: Doctor)

    @Transaction
    @Query("SELECT * FROM doctors")
    fun getDoctorsAndNiches(): Flow<List<DoctorAndNiche>>

    @Transaction
    @Query("SELECT * FROM doctors")
    fun getDoctorsAndQualifications(): Flow<List<DoctorAndQualification>>

    @Transaction
    @Query("SELECT * FROM doctors")
    fun getDoctorsWithDiagnoses(): Flow<List<DoctorWithDiagnoses>>

    @Query("SELECT * FROM doctors WHERE doctorId ==:id")
    suspend fun getDoctor(id: Long): Doctor

    @Query("SELECT * FROM doctors")
    fun getDoctors(): Flow<List<Doctor>>
}