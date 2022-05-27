package com.mapifesto.overpass_datasource

import com.mapifesto.overpass_datasource.EndPoints.INTERPRETER
import io.ktor.client.*
import io.ktor.client.request.*


class OverpassServiceImpl(
    private val httpClient: HttpClient,
): OverpassService {

    override suspend fun getNodeById(id: String): NodesDto {
        return httpClient.get {
            url("$INTERPRETER?data=[out:json][timeout:10];(node($id););out;")
        }
    }

}