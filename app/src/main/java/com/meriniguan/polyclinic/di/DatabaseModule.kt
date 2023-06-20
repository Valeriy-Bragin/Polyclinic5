package com.meriniguan.polyclinic.di

import android.app.Application
import androidx.room.Room
import com.meriniguan.polyclinic.model.AppDatabase
import com.meriniguan.polyclinic.model.diagnosis.room.DiagnosesDao
import com.meriniguan.polyclinic.model.diagnosis.room.utils.DeceasesDao
import com.meriniguan.polyclinic.model.doctor.room.DoctorsDao
import com.meriniguan.polyclinic.model.doctor.room.utils.NichesDao
import com.meriniguan.polyclinic.model.doctor.room.utils.QualificationsDao
import com.meriniguan.polyclinic.model.patient.room.PatientsDao
import com.meriniguan.polyclinic.model.patient.room.utils.ConditionsDao
import com.meriniguan.polyclinic.model.patient.room.utils.SocialStatusesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application
    ): AppDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, "polyclinic_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providePatientsDao(
        appDatabase: AppDatabase
    ): PatientsDao = appDatabase.getPatientsDao()

    @Provides
    @Singleton
    fun provideSocialStatusesDao(
        appDatabase: AppDatabase
    ): SocialStatusesDao = appDatabase.getSocialStatusesDao()

    @Provides
    @Singleton
    fun provideConditionsDao(
        appDatabase: AppDatabase
    ): ConditionsDao = appDatabase.getConditionsDao()

    @Provides
    @Singleton
    fun provideDoctorsDao(
        appDatabase: AppDatabase
    ): DoctorsDao = appDatabase.getDoctorsDao()

    @Provides
    @Singleton
    fun provideNichesDao(
        appDatabase: AppDatabase
    ): NichesDao = appDatabase.getNichesDao()

    @Provides
    @Singleton
    fun provideQualificationsDao(
        appDatabase: AppDatabase
    ): QualificationsDao = appDatabase.getQualificationsDao()

    @Provides
    @Singleton
    fun provideDiagnosesDao(
        appDatabase: AppDatabase
    ): DiagnosesDao = appDatabase.getDiagnosesDao()

    @Provides
    @Singleton
    fun provideDeceasesDao(
        appDatabase: AppDatabase
    ): DeceasesDao = appDatabase.getDeceasesDao()
}