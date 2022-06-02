package com.mapifesto.overpass_datasource

sealed class OverpassDataState<T> {

    data class Error<T>(
        val error: String
    ): OverpassDataState<T>()

    data class OverpassData<T>(
        val data: T
    ): OverpassDataState<T>()

}