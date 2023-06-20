package com.meriniguan.polyclinic.model.diagnosis.room.utils

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import com.meriniguan.polyclinic.model.diagnosis.room.Diagnosis
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "deceases")
data class Decease(
    val name: String,
    @PrimaryKey(autoGenerate = true) val deceaseId: Long = 0
) {
    override fun toString(): String {
        return "$name~$deceaseId"
    }
}

data class DiagnosisAndDecease(
    @Embedded val diagnosis: Diagnosis,
    @Relation(
        parentColumn = "diDeceaseId",
        entityColumn = "deceaseId"
    )
    val decease: Decease
)

@Dao
interface DeceasesDao {
    @Insert
    suspend fun addDecease(decease: Decease)

    @Query("SELECT * FROM deceases")
    fun getDeceases(): Flow<List<Decease>>

    @Query("SELECT * FROM deceases  WHERE deceaseId ==:id")
    suspend fun getDecease(id: Long): Decease
}