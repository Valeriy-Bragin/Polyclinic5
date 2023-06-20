package com.meriniguan.polyclinic.di

import com.meriniguan.polyclinic.model.AppRepository
import com.meriniguan.polyclinic.model.RoomAppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAppRepository(
        tasksRepository: RoomAppRepository
    ): AppRepository
}