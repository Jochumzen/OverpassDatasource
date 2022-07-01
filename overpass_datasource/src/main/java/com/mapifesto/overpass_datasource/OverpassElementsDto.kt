package com.mapifesto.overpass_datasource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OverpassElementsDto(
    @SerialName("version")
    val version: Double,

    @SerialName("generator")
    val generator: String,

    @SerialName("osm3s")
    val osm3s: Osm3s,

    @SerialName("elements")
    val elements: List<ElementDto>
) {

    @Serializable
    data class Osm3s(

        @SerialName("timestamp_osm_base")
        val timestampOsmBase: String,

        @SerialName("copyright")
        val copyright: String,
    )

    @Serializable
    data class ElementDto(

        @SerialName("type")
        val type: String,

        @SerialName("id")
        val id: Long,

        @SerialName("center")
        var center: CenterLatLon? = null,

        @SerialName("lat")
        val lat: Double? = null,

        @SerialName("lon")
        val lon: Double? = null,

        @SerialName("nodes")
        val nodes: List<Long>? = null,

        @SerialName("members")
        val members: List<MemberDto>? = null,

/*        @SerialName("timestamp")
        val timestamp: String?,

        @SerialName("version")
        val version: Int?,

        @SerialName("changeset")
        val changeset: Long?,

        @SerialName("user")
        val user: String?,

        @SerialName("uid")
        val uid: Long?,

        @SerialName("geometry")
        val geometry: List<GeometryLatLon>?,*/

        @SerialName("tags")
        val tags: Map<String, String>,
    )

    @Serializable
    data class CenterLatLon(
        @SerialName("lat")
        val lat: Double?,

        @SerialName("lon")
        val lon: Double?,
    )

/*    @Serializable
    data class GeometryLatLon(
        @SerialName("lat")
        val lat: Double?,

        @SerialName("lon")
        val lon: Double?,
    )*/

    @Serializable
    data class MemberDto(

        @SerialName("type")
        val type: String,

        @SerialName("ref")
        val ref: Long,

        @SerialName("role")
        val role: String,

    )
}


