package com.mapifesto.overpassdatasource

import com.mapifesto.overpass_datasource.OverpassIntermediary
import com.mapifesto.overpass_datasource.OverpassIntermediaryImpl
import com.mapifesto.overpass_datasource.OverpassIntermediaryMockup
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Singleton
    @Provides
    fun provideOverpassIntermediary(

    ) : OverpassIntermediary {
        return OverpassIntermediaryImpl()
    }

}