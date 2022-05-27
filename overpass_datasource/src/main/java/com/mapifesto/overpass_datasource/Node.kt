package com.mapifesto.overpass_datasource

class Node(
    val id: Long,
    val location: LatLon,
    val tags: Map<String,String>,
) {

    companion object {

        fun createFromElementDto(element: NodesDto.ElementDto): Node {
            return Node(
                id = element.id,
                location = LatLon(lat = element.lat, lon = element.lon),
                tags = element.tags
            )
        }
    }

}