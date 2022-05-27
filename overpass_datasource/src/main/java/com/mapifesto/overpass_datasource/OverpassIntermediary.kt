package com.mapifesto.overpass_datasource

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class OverpassIntermediary {

    fun getNodeById(id: Long, callback: (NodeResult) -> Unit) {

        val getNode = OverpassInteractors.build().getNodeById
        getNode.execute(id.toString()).onEach { dataState ->

            when(dataState) {
                is DataState.Error -> {
                    callback(
                        NodeResult(
                            node = null,
                            errorMsg = dataState.error
                        )
                    )
                }
                is DataState.Data -> {
                    callback(
                        NodeResult(
                            node = dataState.data,
                            errorMsg = null
                        )
                    )
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.Main))
    }
}

data class NodeResult(
    val node: Node?,
    val errorMsg: String? = null
)