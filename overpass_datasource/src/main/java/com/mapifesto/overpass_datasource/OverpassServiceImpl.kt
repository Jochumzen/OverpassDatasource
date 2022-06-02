package com.mapifesto.overpass_datasource

import com.mapifesto.overpass_datasource.EndPoints.INTERPRETER
import com.mapifesto.overpass_datasource.node.NodesDto
import com.mapifesto.overpass_datasource.way.WaysDto
import io.ktor.client.*
import io.ktor.client.request.*


class OverpassServiceImpl(
    private val httpClient: HttpClient,
): OverpassService {

    override suspend fun getNodesById(id: String): NodesDto {
        return httpClient.get {
            url("$INTERPRETER?data=[out:json][timeout:10];(node($id););out;")
        }
    }

    override suspend fun getWaysById(id: String): WaysDto {
        return httpClient.get {
            url("$INTERPRETER?data=[out:json][timeout:10];(way($id););out center;")
        }
    }

}