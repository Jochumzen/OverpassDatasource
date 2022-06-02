package com.mapifesto.overpass_datasource.node

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NodesDto(

    @SerialName("version")
    val version: Double,

    @SerialName("generator")
    val generator: String,

    @SerialName("osm3s")
    val osm3s: Osm3s,

    @SerialName("elements")
    val elements: List<ElementDto>,

    ) {

    @Serializable
    data class Osm3s(

        @SerialName("timestamp_osm_base")
        val timestampOsmBase: String?,

        @SerialName("copyright")
        val copyright: String?,

    )

    @Serializable
    data class ElementDto(

        @SerialName("type")
        val type: String,

        @SerialName("id")
        val id: Long,

        @SerialName("lat")
        val lat: Double,

        @SerialName("lon")
        val lon: Double,

        @SerialName("tags")
        val tags: Map<String,String>,
    )
}
