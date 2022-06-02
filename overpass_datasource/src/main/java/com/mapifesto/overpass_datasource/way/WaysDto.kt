package com.mapifesto.overpass_datasource.way

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WaysDto(


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

        @SerialName("center")
        val center: CenterDto,

        @SerialName("nodes")
        val nodes: List<Long>,

        @SerialName("tags")
        val tags: Map<String,String>,

        ) {

            @Serializable
            data class CenterDto(

                @SerialName("lat")
                val lat: Double,

                @SerialName("lon")
                val lon: Double,

            )
    }
}
