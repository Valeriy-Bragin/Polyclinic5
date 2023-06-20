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

@Entity(tableName = "social_statuses")
data class SocialStatus(
    val name: String,
    @PrimaryKey(autoGenerate = true) val socialStatusId: Long = 0
) {
    override fun toString(): String {
        return "$name~$socialStatusId"
    }
}

data class PatientAndSocialStatus(
    @Embedded val patient: Patient,
    @Relation(
        parentColumn = "pSocialStatusId",
        entityColumn = "socialStatusId"
    )
    val socialStatus: SocialStatus
)

@Dao
interface SocialStatusesDao {
    @Insert
    suspend fun addSocialStatus(socialStatus: SocialStatus)

    @Query("SELECT * FROM social_statuses")
    fun getSocialStatuses(): Flow<List<SocialStatus>>

    @Query("SELECT * FROM social_statuses WHERE socialStatusId ==:id")
    suspend fun getSocialStatus(id: Long): SocialStatus
}