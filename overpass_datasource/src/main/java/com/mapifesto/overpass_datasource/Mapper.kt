package com.mapifesto.overpass_datasource

import com.mapifesto.domain.*
import com.mapifesto.overpass_datasource.node.NodesDto
import com.mapifesto.overpass_datasource.relation.RelationsDto
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

    fun createRelation(element: RelationsDto.ElementDto): OverpassRelation {
        return OverpassRelation(
            id = element.id,
            location = LatLon(
                lat = element.center.lat,
                lon = element.center.lon,
            ),
            tags = OsmTags(
                tags = element.tags
            ),
            members = element.members.map { OsmRelationMember(
                type = it.type, ref = it.ref, role = it.role
            )},
            osmElementType = OsmElementType.WAY
        )
    }

    fun createElements(elements: OverpassElementsDto): OverpassElements? {

       /* val x = elements.elements
        val y = x.size

        val z = x.map { it.type}

        val w = x.map { element ->
            when(element.type) {
                "node" -> "node"
                "ways" -> ""
                "relation" -> {
                    val lat = element.center?.lat
                    val lon = element.center?.lon
                    if(lat != null && lon != null) {
                        OverpassRelation(
                            id = element.id,
                            location = LatLon(lat = lat, lon = lon),
                            tags = OsmTags(tags = element.tags),
                            members = element.members?.map { it -> OsmRelationMember(type = it.type, ref = it.ref, role = it.role)}?: listOf(),
                            osmElementType = OsmElementType.RELATION
                        )
                    } else null
                }
                else -> null
            }
        }*/
        val items = elements.elements.map { element ->
            when(element.type) {
                "node" -> {
                    if(element.lat != null && element.lon != null) {
                        OverpassNode(
                            id = element.id,
                            location = LatLon(lat = element.lat, lon = element.lon),
                            tags = OsmTags(tags = element.tags),
                            osmElementType = OsmElementType.NODE
                        )
                    } else {
                        null
                    }
                }
                "way" -> {
                    val lat = element.center?.lat
                    val lon = element.center?.lon
                    if(lat != null && lon != null) {
                        OverpassWay(
                            id = element.id,
                            location = LatLon(lat = lat, lon = lon),
                            tags = OsmTags(tags = element.tags),
                            nodes = element.nodes?: listOf(),
                            osmElementType = OsmElementType.WAY
                        )
                    } else {
                        null
                    }
                }
                "relation" -> {
                    val lat = element.center?.lat
                    val lon = element.center?.lon
                    if(lat != null && lon != null) {
                        OverpassRelation(
                            id = element.id,
                            location = LatLon(lat = lat, lon = lon),
                            tags = OsmTags(tags = element.tags),
                            members = element.members?.map { it -> OsmRelationMember(type = it.type, ref = it.ref, role = it.role)}?: listOf(),
                            osmElementType = OsmElementType.RELATION
                        )
                    } else {
                        null
                    }
                }
                else -> {
                    null
                }
            }

        }
        return if (items.any {it == null}) null else OverpassElements(
            items = items.mapNotNull { it }
        )

    }
}