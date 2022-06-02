package com.mapifesto.overpass_datasource

import com.mapifesto.overpass_datasource.node.GetNodeById
import com.mapifesto.overpass_datasource.way.GetWayById

class OverpassInteractors(
    val getNodeById: GetNodeById,
    val getWayById: GetWayById,
) {

    companion object Factory {
        fun build(): OverpassInteractors{
            val service = OverpassService.build()
            return OverpassInteractors(
                getNodeById = GetNodeById(
                    service = service,
                ),
                getWayById = GetWayById(
                    service = service,
                ),
            )
        }

    }

}