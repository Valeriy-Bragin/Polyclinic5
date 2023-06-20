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

@Entity(tableName = "niches")
data class Niche(
    val name: String,
    @PrimaryKey(autoGenerate = true) val nicheId: Long = 0
) {
    override fun toString(): String {
        return "$name~$nicheId"
    }
}

data class DoctorAndNiche(
    @Embedded val doctor: Doctor,
    @Relation(
        parentColumn = "dNicheId",
        entityColumn = "nicheId"
    )
    val niche: Niche
)

@Dao
interface NichesDao {
    @Insert
    suspend fun addNiche(niche: Niche)

    @Query("SELECT * FROM niches")
    fun getNiches(): Flow<List<Niche>>

    @Query("SELECT * FROM niches WHERE nicheId ==:id")
    suspend fun getNiche(id: Long): Niche
}