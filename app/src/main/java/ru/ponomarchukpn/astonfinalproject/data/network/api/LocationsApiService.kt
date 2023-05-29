package ru.ponomarchukpn.astonfinalproject.data.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ponomarchukpn.astonfinalproject.data.network.dto.LocationDto
import ru.ponomarchukpn.astonfinalproject.data.network.dto.ResponseDto

interface LocationsApiService {

    @GET("location")
    suspend fun loadPage(
        @Query(QUERY_PARAM_PAGE) page: Int
    ): ResponseDto

    @GET("location/{$PATH_ITEM_ID}")
    suspend fun loadItem(
        @Path(PATH_ITEM_ID) itemId: Int
    ): LocationDto

    @GET("location/{$PATH_ITEM_IDS_STRING}")
    suspend fun loadItemsByIds(
        @Path(PATH_ITEM_IDS_STRING) itemIds: String
    ): List<LocationDto>

    companion object {

        private const val QUERY_PARAM_PAGE = "page"
        private const val PATH_ITEM_ID = "itemId"
        private const val PATH_ITEM_IDS_STRING = "itemIdsString"
    }
}
