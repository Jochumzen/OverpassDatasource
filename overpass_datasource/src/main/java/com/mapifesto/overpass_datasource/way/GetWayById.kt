package com.mapifesto.overpass_datasource.way

import com.mapifesto.domain.Way
import com.mapifesto.overpass_datasource.OverpassDataState
import com.mapifesto.overpass_datasource.Mapper
import com.mapifesto.overpass_datasource.OverpassService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWayById(
    private val service: OverpassService,
) {
    fun execute(
        id: String
    ): Flow<OverpassDataState<Way>> = flow {

        var errorMessage: String? = null

        val ways: WaysDto? = try {
            service.getWaysById(id = id )
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = e.message?: "No error message provided"
            null
        }

        if (ways == null) {
            emit(OverpassDataState.Error("Error executing GetWayById. Error message: $errorMessage"))
            return@flow
        }

        //GetNodeById must result in precisely one element
        if(ways.elements.size != 1) {
            emit(OverpassDataState.Error("Error executing GetNodeById. Error message: Number of elements is ${ways.elements.size} when expected is 1"))
            return@flow
        }

        val element = ways.elements[0]

        val way = Mapper.createWay(element)

        emit(OverpassDataState.OverpassData(way))


    }
}