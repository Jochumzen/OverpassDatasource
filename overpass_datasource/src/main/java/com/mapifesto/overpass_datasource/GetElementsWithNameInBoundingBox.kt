package com.mapifesto.overpass_datasource

import com.mapifesto.domain.BoundingBox
import com.mapifesto.domain.OverpassElements
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetElementsWithNameInBoundingBox(
    private val service: OverpassService,
) {
    fun execute(
        boundingBox: BoundingBox
    ): Flow<OverpassDataState<OverpassElements>> = flow {

        var errorMessage: String? = null

        val elements: OverpassElementsDto? = try {
            service.getElementsWithName(
                south = boundingBox.southWest.lat,
                west = boundingBox.southWest.lon,
                north = boundingBox.northEast.lat,
                east = boundingBox.northEast.lon,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = e.message?: "No error message provided"
            null
        }

        if (elements == null) {
            emit(OverpassDataState.Error("Error executing GetElementsWithNameInBoundingBox. Error message: $errorMessage"))
            return@flow
        }

        val overpassElements = Mapper.createElements(elements)

        if(overpassElements == null)
            emit(OverpassDataState.Error("Error executing GetElementsWithNameInBoundingBox. Failed to map dto to domain"))
        else
            emit(OverpassDataState.Data(overpassElements))

    }
}