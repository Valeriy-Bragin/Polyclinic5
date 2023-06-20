package com.meriniguan.polyclinic.model.patient.room.utils

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import com.meriniguan.polyclinic.model.patient.room.Patient
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "conditions")
data class Condition(
    val name: String,
    @PrimaryKey(autoGenerate = true) val conditionId: Long = 0
) {
    override fun toString(): String {
        return "$name~$conditionId"
    }
}

data class PatientAndCondition(
    @Embedded val patient: Patient,
    @Relation(
        parentColumn = "pConditionId",
        entityColumn = "conditionId"
    )
    val condition: Condition
)

@Dao
interface ConditionsDao {
    @Insert
    suspend fun addCondition(condition: Condition)

    @Query("SELECT * FROM conditions")
    fun getConditions(): Flow<List<Condition>>

    @Query("SELECT * FROM conditions WHERE conditionId ==:id")
    suspend fun getCondition(id: Long): Condition
}