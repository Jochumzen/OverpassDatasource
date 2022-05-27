package com.mapifesto.overpass_datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNodeById(
    private val service: OverpassService,
) {
    fun execute(
        id: String
    ): Flow<DataState<Node>> = flow {

        var errorMessage: String? = null

        val nodes: NodesDto? = try {
            service.getNodeById(id = id )
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = e.message?: "No error message provided"
            null
        }

        if (nodes == null) {
            emit(DataState.Error("Error executing GetNodeById. Error message: $errorMessage"))
            return@flow
        }

        //GetNodeById must result in precisely one element
        if(nodes.elements.size != 1) {
            emit(DataState.Error("Error executing GetNodeById. Error message: Number of elements is ${nodes.elements.size} when expected is 1"))
            return@flow
        }

        val element = nodes.elements[0]

        val node = Node.createFromElementDto(element)

        emit(DataState.Data(node))


    }
}