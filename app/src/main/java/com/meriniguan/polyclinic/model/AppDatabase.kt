package com.meriniguan.polyclinic.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meriniguan.polyclinic.model.diagnosis.room.DiagnosesDao
import com.meriniguan.polyclinic.model.diagnosis.room.Diagnosis
import com.meriniguan.polyclinic.model.diagnosis.room.utils.Decease
import com.meriniguan.polyclinic.model.diagnosis.room.utils.DeceasesDao
import com.meriniguan.polyclinic.model.doctor.room.Doctor
import com.meriniguan.polyclinic.model.doctor.room.DoctorsDao
import com.meriniguan.polyclinic.model.doctor.room.utils.Niche
import com.meriniguan.polyclinic.model.doctor.room.utils.NichesDao
import com.meriniguan.polyclinic.model.doctor.room.utils.Qualification
import com.meriniguan.polyclinic.model.doctor.room.utils.QualificationsDao
import com.meriniguan.polyclinic.model.patient.room.Patient
import com.meriniguan.polyclinic.model.patient.room.PatientsDao
import com.meriniguan.polyclinic.model.patient.room.utils.Condition
import com.meriniguan.polyclinic.model.patient.room.utils.ConditionsDao
import com.meriniguan.polyclinic.model.patient.room.utils.SocialStatus
import com.meriniguan.polyclinic.model.patient.room.utils.SocialStatusesDao

@Database(
    entities = [
        Patient::class,
        SocialStatus::class,
        Condition::class,
        Doctor::class,
        Niche::class,
        Qualification::class,
        Diagnosis::class,
        Decease::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getPatientsDao(): PatientsDao
    abstract fun getSocialStatusesDao(): SocialStatusesDao
    abstract fun getConditionsDao(): ConditionsDao
    abstract fun getDoctorsDao(): DoctorsDao
    abstract fun getNichesDao(): NichesDao
    abstract fun getQualificationsDao(): QualificationsDao
    abstract fun getDiagnosesDao(): DiagnosesDao
    abstract fun getDeceasesDao(): DeceasesDao
}