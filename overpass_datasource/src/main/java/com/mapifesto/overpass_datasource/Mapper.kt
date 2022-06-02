package com.mapifesto.overpass_datasource

import com.mapifesto.domain.LatLon
import com.mapifesto.domain.Node
import com.mapifesto.domain.OsmTags
import com.mapifesto.domain.Way
import com.mapifesto.overpass_datasource.node.NodesDto
import com.mapifesto.overpass_datasource.way.WaysDto

object Mapper {

    fun createNode(element: NodesDto.ElementDto): Node {
        return Node(
            id = element.id,
            location = LatLon(
                lat = element.lat,
                lon = element.lon,
            ),
            tags = OsmTags(
                tags = element.tags
            )
        )
    }

    fun createWay(element: WaysDto.ElementDto): Way {
        return Way(
            id = element.id,
            location = LatLon(
                lat = element.center.lat,
                lon = element.center.lon,
            ),
            tags = OsmTags(
                tags = element.tags
            ),
            nodes = element.nodes
        )
    }
}