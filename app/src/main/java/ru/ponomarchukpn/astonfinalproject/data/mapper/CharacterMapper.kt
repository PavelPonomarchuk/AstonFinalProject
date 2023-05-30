package ru.ponomarchukpn.astonfinalproject.data.mapper

import com.google.gson.Gson
import ru.ponomarchukpn.astonfinalproject.common.getIdListFromUrls
import ru.ponomarchukpn.astonfinalproject.common.getLocationIdFromUrl
import ru.ponomarchukpn.astonfinalproject.common.hasNextPage
import ru.ponomarchukpn.astonfinalproject.data.database.CharacterDbModel
import ru.ponomarchukpn.astonfinalproject.data.network.dto.CharacterDto
import ru.ponomarchukpn.astonfinalproject.data.network.dto.CharactersPageDto
import ru.ponomarchukpn.astonfinalproject.data.network.dto.CharactersResponseDto
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterGender
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterMapper @Inject constructor() {

    fun mapCharactersPageToResponseDto(charactersPage: CharactersPageDto): CharactersResponseDto {
        val resultList = mutableListOf<CharacterEntity>()
        val hasNextPage = charactersPage.info.hasNextPage()

        charactersPage.results.forEach { characterJsonObject ->
            Gson().fromJson(characterJsonObject, CharacterDto::class.java)?.let {
                resultList.add(mapDtoToEntity(it))
            }
        }
        return CharactersResponseDto(hasNextPage, resultList)
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
        originId = dto.origin.getLocationIdFromUrl(),
        locationId = dto.location.getLocationIdFromUrl(),
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
        originId = dto.origin.getLocationIdFromUrl(),
        locationId = dto.location.getLocationIdFromUrl(),
        imageUrl = dto.image,
        episodesId = dto.episode.getIdListFromUrls().joinToString(),
        url = dto.url,
        created = dto.created,
        relatedToPage = page
    )
}
