package ru.ponomarchukpn.astonfinalproject.data.mapper

import com.google.gson.Gson
import ru.ponomarchukpn.astonfinalproject.common.getIdListFromUrls
import ru.ponomarchukpn.astonfinalproject.data.database.EpisodeDbModel
import ru.ponomarchukpn.astonfinalproject.data.network.dto.EpisodeDto
import ru.ponomarchukpn.astonfinalproject.data.network.dto.ResponseDto
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpisodeMapper @Inject constructor() {

    //TODO прибрать

    fun mapPageToEntitiesList(page: ResponseDto) = mutableListOf<EpisodeEntity>().apply {
        page.results.forEach { episodeJsonObject ->
            Gson().fromJson(episodeJsonObject, EpisodeDto::class.java)?.let {
                this.add(mapDtoToEntity(it))
            }
        }
    }.toList()

    fun mapDtoListToEntityList(dtoList: List<EpisodeDto>) = dtoList.map { mapDtoToEntity(it) }

    fun mapDtoToEntity(dto: EpisodeDto) = EpisodeEntity(
        id = dto.id,
        name = dto.name,
        airDate = dto.airDate,
        episode = dto.episode,
        charactersId = dto.characters.getIdListFromUrls(),
        url = dto.url,
        created = dto.created
    )

    fun mapPageToDbModelsList(page: ResponseDto) =
        mutableListOf<EpisodeDbModel>().apply {
            page.results.forEach { episodeJsonObject ->
                Gson().fromJson(episodeJsonObject, EpisodeDto::class.java)?.let {
                    this.add(mapDtoToDbModel(it))
                }
            }
        }.toList()

    fun mapDtoListToDbModelList(dtoList: List<EpisodeDto>) = dtoList.map { mapDtoToDbModel(it) }

    private fun mapDtoToDbModel(dto: EpisodeDto) = EpisodeDbModel(
        id = dto.id,
        name = dto.name,
        airDate = dto.airDate,
        episode = dto.episode,
        charactersId = dto.characters.getIdListFromUrls().joinToString(),
        url = dto.url,
        created = dto.created
    )

    fun mapDbModelListToEntityList(dbModels: List<EpisodeDbModel>) = dbModels.map {
        mapDbModelToEntity(it)
    }

    fun mapDbModelToEntity(dbModel: EpisodeDbModel) = EpisodeEntity(
        id = dbModel.id,
        name = dbModel.name,
        airDate = dbModel.airDate,
        episode = dbModel.episode,
        charactersId = if (dbModel.charactersId != "") {
            dbModel.charactersId.split(",").map { it.trim().toInt() }
        } else {
               emptyList()
        },
        url = dbModel.url,
        created = dbModel.created
    )
}
