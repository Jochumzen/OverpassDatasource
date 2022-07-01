package com.mapifesto.overpass_datasource

import com.mapifesto.overpass_datasource.node.GetNodeById
import com.mapifesto.overpass_datasource.relation.GetRelationById
import com.mapifesto.overpass_datasource.way.GetWayById

class OverpassInteractors(
    val getNodeById: GetNodeById,
    val getWayById: GetWayById,
    val getRelationById: GetRelationById,
    val getElementsWithNameInBoundingBox: GetElementsWithNameInBoundingBox,
    val getElementsSatisfyingTagRestrictions: GetElementsSatisfyingTagRestrictions,
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
                getRelationById = GetRelationById(
                    service = service,
                ),
                getElementsWithNameInBoundingBox = GetElementsWithNameInBoundingBox(
                    service = service,
                ),
                getElementsSatisfyingTagRestrictions = GetElementsSatisfyingTagRestrictions(
                    service = service,
                ),
            )
        }

    }

}