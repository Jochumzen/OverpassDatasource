package com.mapifesto.overpass_datasource

import com.mapifesto.domain.*
import com.mapifesto.overpass_datasource.node.NodesDto
import com.mapifesto.overpass_datasource.way.WaysDto

object Mapper {

    fun createNode(element: NodesDto.ElementDto): OverpassNode {
        return OverpassNode(
            id = element.id,
            location = LatLon(
                lat = element.lat,
                lon = element.lon,
            ),
            tags = OsmTags(
                tags = element.tags
            ),
            osmElementType = OsmElementType.NODE
        )
    }

    fun createWay(element: WaysDto.ElementDto): OverpassWay {
        return OverpassWay(
            id = element.id,
            location = LatLon(
                lat = element.center.lat,
                lon = element.center.lon,
            ),
            tags = OsmTags(
                tags = element.tags
            ),
            nodes = element.nodes,
            osmElementType = OsmElementType.WAY
        )
    }
}