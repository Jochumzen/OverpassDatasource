package com.mapifesto.overpass_datasource

class OverpassInteractors(
    val getNodeById: GetNodeById,
) {

    companion object Factory {
        fun build(): OverpassInteractors{
            val service = OverpassService.build()
            return OverpassInteractors(
                getNodeById = GetNodeById(
                    service = service,
                ),
            )
        }

    }

}