package com.mapifesto.overpass_datasource

import com.mapifesto.domain.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface OverpassIntermediary {

    fun getElementByIdAndType(id: Long, type: String, callback: (OverpassDataState<OverpassElement>) -> Unit)

    fun getElementsWithNameInBoundingBox(boundingBox: BoundingBox, callback: (OverpassDataState<OverpassElements>) -> Unit)

    fun getElementsSatisfyingTagRestrictions(boundingBox: BoundingBox, tagRestrictions: IntersectionOfOverpassTagRestrictions, callback: (OverpassDataState<OverpassElements>) -> Unit)
}

class OverpassIntermediaryImpl: OverpassIntermediary {

    private var callback: (OverpassDataState<OverpassElement>) -> Unit = {}

    override fun getElementByIdAndType(id: Long, type: String, callback: (OverpassDataState<OverpassElement>) -> Unit) {
        this.callback = callback

        when (type) {
            "node" -> getNodeById(id)
            "way" -> getWayById(id)
            "relation" -> getRelationById(id)
            else -> {}
        }
    }

    override fun getElementsWithNameInBoundingBox(
        boundingBox: BoundingBox,
        callback: (OverpassDataState<OverpassElements>) -> Unit
    ) {
        val getElementsWithNameInBoundingBox = OverpassInteractors.build().getElementsWithNameInBoundingBox
        getElementsWithNameInBoundingBox.execute(boundingBox).onEach { dataState ->

            when(dataState) {
                is OverpassDataState.Error -> {
                    callback(OverpassDataState.Error(dataState.error))
                }
                is OverpassDataState.Data -> {
                    callback(OverpassDataState.Data(dataState.data.cleanList()))
                }
            }

        }.launchIn(CoroutineScope(Dispatchers.Main))
    }

    override fun getElementsSatisfyingTagRestrictions(
        boundingBox: BoundingBox,
        tagRestrictions: IntersectionOfOverpassTagRestrictions,
        callback: (OverpassDataState<OverpassElements>) -> Unit
    ) {
        val getElementsWithNameInBoundingBox = OverpassInteractors.build().getElementsSatisfyingTagRestrictions
        getElementsWithNameInBoundingBox.execute(boundingBox, tagRestrictions).onEach { dataState ->

            when(dataState) {
                is OverpassDataState.Error -> {
                    callback(OverpassDataState.Error(dataState.error))
                }
                is OverpassDataState.Data -> {
                    callback(OverpassDataState.Data(dataState.data))
                }
            }

        }.launchIn(CoroutineScope(Dispatchers.Main))
    }

    private fun getNodeById(id: Long) {

        val getNode = OverpassInteractors.build().getNodeById
        getNode.execute(id.toString()).onEach { dataState ->

            when(dataState) {
                is OverpassDataState.Error -> {
                    callback(OverpassDataState.Error(dataState.error))
                }
                is OverpassDataState.Data -> {
                    callback(OverpassDataState.Data(dataState.data))
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
                is OverpassDataState.Data -> {
                    callback(OverpassDataState.Data(dataState.data))
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.Main))
    }

    private fun getRelationById(id: Long) {

        val getRelation = OverpassInteractors.build().getRelationById
        getRelation.execute(id.toString()).onEach { dataState ->

            when(dataState) {
                is OverpassDataState.Error -> {
                    callback(OverpassDataState.Error(dataState.error))
                }
                is OverpassDataState.Data -> {
                    callback(OverpassDataState.Data(dataState.data))
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.Main))
    }

}

class OverpassIntermediaryMockup: OverpassIntermediary {
    override fun getElementByIdAndType(id: Long, type: String, callback: (OverpassDataState<OverpassElement>) -> Unit) {
        when(type) {
            "node" ->
                callback(
                    OverpassDataState.Data(
                        data = OverpassNode(
                            id = id,
                            location = LatLon(
                                lat = 55.1,
                                lon = 32.1
                            ),
                            tags = OsmTags(
                                tags = mapOf("name" to "Mockup store node", "amenity" to "bar", "opening_hours" to "Mo-Su 10.00-19.00")
                            ),
                            osmElementType = OsmElementType.NODE
                        )
                    )
                )
            "way" ->
                callback(
                    OverpassDataState.Data(
                        data = OverpassWay(
                            id = id,
                            location = LatLon(
                                lat = 55.1,
                                lon = 32.1
                            ),
                            tags = OsmTags(
                                tags = mapOf("name" to "Mockup store way", "shop" to "supermarket")
                            ),
                            nodes = listOf(),
                            osmElementType = OsmElementType.WAY
                        )
                    )
                )
        }

    }

    override fun getElementsWithNameInBoundingBox(
        boundingBox: BoundingBox,
        callback: (OverpassDataState<OverpassElements>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun getElementsSatisfyingTagRestrictions(
        boundingBox: BoundingBox,
        tagRestrictions: IntersectionOfOverpassTagRestrictions,
        callback: (OverpassDataState<OverpassElements>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

}


