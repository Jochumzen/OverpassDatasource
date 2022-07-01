package com.mapifesto.overpass_datasource

import com.mapifesto.domain.OsmTag
import com.mapifesto.overpass_datasource.EndPoints.INTERPRETER
import com.mapifesto.overpass_datasource.node.NodesDto
import com.mapifesto.overpass_datasource.relation.RelationsDto
import com.mapifesto.overpass_datasource.way.WaysDto
import io.ktor.client.*
import io.ktor.client.request.*


class OverpassServiceImpl(
    private val httpClient: HttpClient,
): OverpassService {

    override suspend fun getNodesById(id: String): NodesDto {
        return httpClient.get {
            url(INTERPRETER)
            parameter(key = "data", value = "[out:json][timeout:10];(node($id););out;")
        }
    }

    override suspend fun getWaysById(id: String): WaysDto {
        return httpClient.get {
            url(INTERPRETER)
            parameter(key = "data", value = "[out:json][timeout:10];(way($id););out center;")
        }
    }

    override suspend fun getRelationsById(id: String): RelationsDto {
        return httpClient.get {
            url(INTERPRETER)
            parameter(key = "data", value = "[out:json][timeout:10];(relation($id););out center;")
        }
    }

    override suspend fun getElementsWithName(
        south: Double,
        west: Double,
        north: Double,
        east: Double
    ): OverpassElementsDto {
        return httpClient.get {
            val boundingBox = "($south,$west,$north,$east)"
            val node = "node[name]$boundingBox;"
            val way = "way[name]$boundingBox;"
            url(INTERPRETER)
            parameter(
                key = "data",
                value = "[out:json][timeout:20];($node$way);out center;")
        }
    }

    override suspend fun getElementsSatisfyingTagRestrictions(
        south: Double,
        west: Double,
        north: Double,
        east: Double,
        tagRestrictions: IntersectionOfOverpassTagRestrictions
    ): OverpassElementsDto {
        val boundingBox = "($south,$west,$north,$east)"
        val data = tagRestrictions.overpassString(boundingBox)
        val z = 1
        return httpClient.get {
            url(INTERPRETER)
            parameter(
                key = "data",
                value = data
            )
        }
    }

}