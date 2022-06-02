package com.mapifesto.overpass_datasource

import com.mapifesto.overpass_datasource.node.NodesDto
import com.mapifesto.overpass_datasource.way.WaysDto
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

interface OverpassService {

    suspend fun getNodesById(id: String): NodesDto

    suspend fun getWaysById(id: String): WaysDto

    companion object Factory {
        fun build(): OverpassService {
            return OverpassServiceImpl (
                httpClient = HttpClient(Android) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(
                            kotlinx.serialization.json.Json {
                                ignoreUnknownKeys = false
                                isLenient = false
                            }
                        )
                    }
                    //install(HttpTimeout)
                }
            )
        }
    }
}