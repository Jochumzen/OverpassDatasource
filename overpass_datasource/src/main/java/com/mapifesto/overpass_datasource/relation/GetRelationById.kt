package com.mapifesto.overpass_datasource.relation

import com.mapifesto.domain.OverpassRelation
import com.mapifesto.overpass_datasource.OverpassDataState
import com.mapifesto.overpass_datasource.Mapper
import com.mapifesto.overpass_datasource.OverpassService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRelationById(
    private val service: OverpassService,
) {
    fun execute(
        id: String
    ): Flow<OverpassDataState<OverpassRelation>> = flow {

        var errorMessage: String? = null

        val relations: RelationsDto? = try {
            service.getRelationsById(id = id )
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = e.message?: "No error message provided"
            null
        }

        if (relations == null) {
            emit(OverpassDataState.Error("Error executing GetRelationById. Error message: $errorMessage"))
            return@flow
        }

        //GetNodeById must result in precisely one element
        if(relations.elements.size != 1) {
            emit(OverpassDataState.Error("Error executing GetRelationById. Error message: Number of elements is ${relations.elements.size} when expected is 1"))
            return@flow
        }

        val element = relations.elements[0]

        val relation = Mapper.createRelation(element)

        emit(OverpassDataState.Data(relation))

    }
}

class Changeset {

}

class GetChangesetById(
    private val service: OverpassService,
) {
    fun execute(
         id: String
    ): Flow<OverpassDataState<Changeset>> = flow {

        var errorMessage: String? = null

        //Create the Dto object here in try catch

        //Emit  DataState.Error in case anything is wrong

        //Create the domain object and DataState.Data

    }
}