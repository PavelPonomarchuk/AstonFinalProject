package ru.ponomarchukpn.astonfinalproject.data.mapper

import com.google.gson.Gson
import ru.ponomarchukpn.astonfinalproject.data.database.CharacterDbModel
import ru.ponomarchukpn.astonfinalproject.data.network.dto.CharacterDto
import ru.ponomarchukpn.astonfinalproject.data.network.dto.CharactersPageDto
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterGender
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterStatus
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharactersPageResponse
import ru.ponomarchukpn.astonfinalproject.utils.getIdListFromUrls
import ru.ponomarchukpn.astonfinalproject.utils.getLocationId

//TODO сделать обычным классом и поставлять через di
object CharacterMapper {

    private const val KEY_NEXT = "next"

    fun mapCharactersPageToResponseEntity(charactersPage: CharactersPageDto): CharactersPageResponse {
        val resultList = mutableListOf<CharacterEntity>()
        val hasNextPage = charactersPage.info.get(KEY_NEXT).asString != null

        charactersPage.results.forEach { characterJsonObject ->
            Gson().fromJson(characterJsonObject, CharacterDto::class.java)?.let {
                resultList.add(mapDtoToEntity(it))
            }
        }
        return CharactersPageResponse(hasNextPage, resultList)
    }

    private fun mapDtoToEntity(dto: CharacterDto) = CharacterEntity(
        id = dto.id,
        name = dto.name,
        status = when (dto.status) {
            "Alive" -> CharacterStatus.ALIVE
            "Dead" -> CharacterStatus.DEAD
            "unknown" -> CharacterStatus.UNKNOWN
            else -> throw RuntimeException("Wrong status value: ${dto.status}")
        },
        species = dto.species,
        type = dto.type,
        gender = when(dto.gender) {
            "Female" -> CharacterGender.FEMALE
            "Male" -> CharacterGender.MALE
            "Genderless" -> CharacterGender.GENDERLESS
            "unknown" -> CharacterGender.UNKNOWN
            else -> throw RuntimeException("Wrong gender value: ${dto.gender}")
        },
        originId = dto.origin.getLocationId(),
        locationId = dto.location.getLocationId(),
        imageUrl = dto.image,
        episodesId = dto.episode.getIdListFromUrls(),
        url = dto.url,
        created = dto.created
    )

    private fun mapDtoToDbModel(dto: CharacterDto, page: Int) = CharacterDbModel(
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
        gender = when(dto.gender) {
            "Female" -> CharacterGender.FEMALE.ordinal
            "Male" -> CharacterGender.MALE.ordinal
            "Genderless" -> CharacterGender.GENDERLESS.ordinal
            "unknown" -> CharacterGender.UNKNOWN.ordinal
            else -> throw RuntimeException("Wrong gender value: ${dto.gender}")
        },
        originId = dto.origin.getLocationId(),
        locationId = dto.location.getLocationId(),
        imageUrl = dto.image,
        episodesId = dto.episode.getIdListFromUrls().joinToString(),
        url = dto.url,
        created = dto.created,
        relatedToPage = page
    )
}
