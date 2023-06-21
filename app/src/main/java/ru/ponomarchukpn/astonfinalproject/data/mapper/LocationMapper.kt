package ru.ponomarchukpn.astonfinalproject.data.mapper

import com.google.gson.Gson
import ru.ponomarchukpn.astonfinalproject.common.getIdListFromUrls
import ru.ponomarchukpn.astonfinalproject.data.database.LocationDbModel
import ru.ponomarchukpn.astonfinalproject.data.network.dto.LocationDto
import ru.ponomarchukpn.astonfinalproject.data.network.dto.ResponseDto
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationMapper @Inject constructor() {

    //TODO почистить

    fun mapPageToLocationsList(page: ResponseDto): List<LocationEntity> {
        val resultList = mutableListOf<LocationEntity>()

        page.results.forEach { locationJsonObject ->
            Gson().fromJson(locationJsonObject, LocationDto::class.java)?.let {
                resultList.add(mapDtoToEntity(it))
            }
        }
        return resultList.toList()
    }

    fun mapDtoToEntity(dto: LocationDto) = LocationEntity(
        id = dto.id,
        name = dto.name,
        type = dto.type,
        dimension = dto.dimension,
        residentsId = dto.residents.getIdListFromUrls(),
        url = dto.url,
        created = dto.created
    )

    fun mapPageToDbModelList(page: ResponseDto) =
        mutableListOf<LocationDbModel>().apply {
            page.results.forEach { locationJsonObject ->
                Gson().fromJson(locationJsonObject, LocationDto::class.java)?.let {
                    this.add(mapDtoToDbModel(it))
                }
            }
        }.toList()

    fun mapDtoListToDbModelList(dtoList: List<LocationDto>) = dtoList.map { mapDtoToDbModel(it) }

    private fun mapDtoToDbModel(dto: LocationDto) = LocationDbModel(
        id = dto.id,
        name = dto.name,
        type = dto.type,
        dimension = dto.dimension,
        residentsId = dto.residents.getIdListFromUrls().joinToString(),
        url = dto.url,
        created = dto.created
    )

    fun mapDbModelListToEntityList(dbModels: List<LocationDbModel>) = dbModels.map {
        mapDbModelToEntity(it)
    }

    fun mapDbModelToEntity(dbModel: LocationDbModel) = LocationEntity(
        id = dbModel.id,
        name = dbModel.name,
        type = dbModel.type,
        dimension = dbModel.dimension,
        residentsId = dbModel.residentsId.split(",").map { it.trim().toInt() },
        url = dbModel.url,
        created = dbModel.created
    )
}
