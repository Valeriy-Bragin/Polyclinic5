package com.meriniguan.polyclinic.model.doctor.room.utils

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import com.meriniguan.polyclinic.model.doctor.room.Doctor
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "qualifications")
data class Qualification(
    val name: String,
    @PrimaryKey(autoGenerate = true) val qualificationId: Long = 0
) {
    override fun toString(): String {
        return "$name~$qualificationId"
    }
}

data class DoctorAndQualification(
    @Embedded val doctor: Doctor,
    @Relation(
        parentColumn = "dQualificationId",
        entityColumn = "qualificationId"
    )
    val qualification: Qualification
)

@Dao
interface QualificationsDao {
    @Insert
    suspend fun addQualification(qualification: Qualification)

    @Query("SELECT * FROM qualifications")
    fun getQualifications(): Flow<List<Qualification>>

    @Query("SELECT * FROM qualifications WHERE qualificationId ==:id")
    suspend fun getQualification(id: Long): Qualification
}