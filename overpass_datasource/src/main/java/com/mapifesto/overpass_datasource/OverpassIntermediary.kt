package com.mapifesto.overpass_datasource

import com.mapifesto.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface OverpassIntermediary {

    fun getElementByIdAndType(id: Long, type: String, callback: (OverpassDataState<OsmElement>) -> Unit)

}

class OverpassIntermediaryImpl: OverpassIntermediary {

    private var callback: (OverpassDataState<OsmElement>) -> Unit = {}

    override fun getElementByIdAndType(id: Long, type: String, callback: (OverpassDataState<OsmElement>) -> Unit) {
        this.callback = callback

        when (type) {
            "node" -> getNodeById(id)
            "way" -> getWayById(id)
            else -> {}
        }
    }

    private fun getNodeById(id: Long) {

        val getNode = OverpassInteractors.build().getNodeById
        getNode.execute(id.toString()).onEach { dataState ->

            when(dataState) {
                is OverpassDataState.Error -> {
                    callback(OverpassDataState.Error(dataState.error))
                }
                is OverpassDataState.OverpassData -> {
                    callback(OverpassDataState.OverpassData(dataState.data))
                }
            }

        }.launchIn(CoroutineScope(Dispatchers.Main))
    }

    private fun getWayById(id: Long) {

        val getWay = OverpassInteractors.build().getWayById
        getWay.execute(id.toString()).onEach { dataState ->

            when(dataState) {
                is OverpassDataState.Error -> {
                    callback(OverpassDataState.Error(dataState.error))
                }
                is OverpassDataState.OverpassData -> {
                    callback(OverpassDataState.OverpassData(dataState.data))
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.Main))
    }
}

class OverpassIntermediaryMockup: OverpassIntermediary {
    override fun getElementByIdAndType(id: Long, type: String, callback: (OverpassDataState<OsmElement>) -> Unit) {
        when(type) {
            "node" ->
                callback(
                    OverpassDataState.OverpassData(
                        data = Node(
                            id = id,
                            location = LatLon(
                                lat = 55.1,
                                lon = 32.1
                            ),
                            tags = OsmTags(
                                tags = mapOf("name" to "Mockup store node", "amenity" to "bar", "opening_hours" to "Mo-Su 10.00-19.00")
                            )
                        )
                    )
                )
            "way" ->
                callback(
                    OverpassDataState.OverpassData(
                        data = Way(
                            id = id,
                            location = LatLon(
                                lat = 55.1,
                                lon = 32.1
                            ),
                            tags = OsmTags(
                                tags = mapOf("name" to "Mockup store way", "shop" to "supermarket")
                            ),
                            nodes = listOf()
                        )
                    )
                )
        }

    }

}


