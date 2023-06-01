package ru.ponomarchukpn.astonfinalproject.data.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ponomarchukpn.astonfinalproject.data.network.dto.LocationDto
import ru.ponomarchukpn.astonfinalproject.data.network.dto.ResponseDto

interface LocationsApiService {

    @GET("location")
    suspend fun loadLocationsPage(
        @Query(QUERY_PARAM_PAGE) page: Int
    ): ResponseDto

    @GET("location/{locationId}")
    suspend fun loadSingleLocation(
        @Path(PATH_LOCATION_ID) locationId: Int
    ): LocationDto

    companion object {

        private const val QUERY_PARAM_PAGE = "page"
        private const val PATH_LOCATION_ID = "locationId"
    }
}
