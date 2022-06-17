package com.mapifesto.overpass_datasource.node

import com.mapifesto.domain.OverpassNode
import com.mapifesto.overpass_datasource.OverpassDataState
import com.mapifesto.overpass_datasource.Mapper
import com.mapifesto.overpass_datasource.OverpassService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNodeById(
    private val service: OverpassService,
) {
    fun execute(
        id: String
    ): Flow<OverpassDataState<OverpassNode>> = flow {

        var errorMessage: String? = null

        val nodes: NodesDto? = try {
            service.getNodesById(id = id )
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = e.message?: "No error message provided"
            null
        }

        if (nodes == null) {
            emit(OverpassDataState.Error("Error executing GetNodeById. Error message: $errorMessage"))
            return@flow
        }

        //GetNodeById must result in precisely one element
        if(nodes.elements.size != 1) {
            emit(OverpassDataState.Error("Error executing GetNodeById. Error message: Number of elements is ${nodes.elements.size} when expected is 1"))
            return@flow
        }

        val element = nodes.elements[0]

        val node = Mapper.createNode(element)

        emit(OverpassDataState.Data(node))

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