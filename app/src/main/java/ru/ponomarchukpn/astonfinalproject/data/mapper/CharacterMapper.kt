package ru.ponomarchukpn.astonfinalproject.data.mapper

import com.google.gson.Gson
import ru.ponomarchukpn.astonfinalproject.common.getIdListFromUrls
import ru.ponomarchukpn.astonfinalproject.common.idUrlEndsWith
import ru.ponomarchukpn.astonfinalproject.data.database.CharacterDbModel
import ru.ponomarchukpn.astonfinalproject.data.network.dto.CharacterDto
import ru.ponomarchukpn.astonfinalproject.data.network.dto.ResponseDto
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterGender
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterMapper @Inject constructor() {

    fun mapPageToDbModelList(page: ResponseDto) = mutableListOf<CharacterDbModel>().apply {
        page.results.forEach { json ->
            Gson().fromJson(json, CharacterDto::class.java)?.let {
                this.add(mapDtoToDbModel(it))
            }
        }
    }.toList()

    fun mapDtoListToDbModelList(dtoList: List<CharacterDto>) = dtoList.map { mapDtoToDbModel(it) }

    private fun mapDtoToDbModel(dto: CharacterDto) = CharacterDbModel(
        id = dto.id,
        name = dto.name,
        status = when (dto.status) {
            "Alive" -> CharacterStatus.ALIVE.ordinal
            "Dead" -> CharacterStatus.DEAD.ordinal
            "unknown" -> CharacterStatus.UNKNOWN.ordinal
            else -> throw RuntimeException("Wrong status value: ${dto.status}")
        },
        species = dto.species,
        type = dto.type,
        gender = when (dto.gender) {
            "Female" -> CharacterGender.FEMALE.ordinal
            "Male" -> CharacterGender.MALE.ordinal
            "Genderless" -> CharacterGender.GENDERLESS.ordinal
            "unknown" -> CharacterGender.UNKNOWN.ordinal
            else -> throw RuntimeException("Wrong gender value: ${dto.gender}")
        },
        originId = dto.origin.url.idUrlEndsWith(),
        locationId = dto.location.url.idUrlEndsWith(),
        imageUrl = dto.image,
        episodesId = dto.episode.getIdListFromUrls().joinToString(),
        url = dto.url,
        created = dto.created
    )

    fun mapDbModelsListToEntitiesList(dbModels: List<CharacterDbModel>) = dbModels.map {
        mapDbModelToEntity(it)
    }

    fun mapDbModelToEntity(dbModel: CharacterDbModel) = CharacterEntity(
        id = dbModel.id,
        name = dbModel.name,
        status = when (dbModel.status) {
            CharacterStatus.ALIVE.ordinal -> CharacterStatus.ALIVE
            CharacterStatus.DEAD.ordinal -> CharacterStatus.DEAD
            CharacterStatus.UNKNOWN.ordinal -> CharacterStatus.UNKNOWN
            else -> throw RuntimeException("Wrong status value: ${dbModel.status}")
        },
        species = dbModel.species,
        type = dbModel.type,
        gender = when (dbModel.gender) {
            CharacterGender.FEMALE.ordinal -> CharacterGender.FEMALE
            CharacterGender.MALE.ordinal -> CharacterGender.MALE
            CharacterGender.GENDERLESS.ordinal -> CharacterGender.GENDERLESS
            CharacterGender.UNKNOWN.ordinal -> CharacterGender.UNKNOWN
            else -> throw RuntimeException("Wrong gender value: ${dbModel.gender}")
        },
        originId = dbModel.originId,
        locationId = dbModel.locationId,
        imageUrl = dbModel.imageUrl,
        episodesId = dbModel.episodesId.split(",").map { it.trim().toInt() },
        url = dbModel.url,
        created = dbModel.created
    )
}
