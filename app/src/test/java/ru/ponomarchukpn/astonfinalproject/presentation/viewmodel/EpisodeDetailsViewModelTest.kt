package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import app.cash.turbine.test
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterGender
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterStatus
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetCharactersByIdUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetSingleEpisodeUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.LoadCharactersByIdUseCase

class EpisodeDetailsViewModelTest : BehaviorSpec({

    val getSingleEpisodeUseCase = mockk<GetSingleEpisodeUseCase>()
    val getCharactersByIdUseCase = mockk<GetCharactersByIdUseCase>()
    val loadCharactersByIdUseCase = mockk<LoadCharactersByIdUseCase>(relaxed = true)

    val viewModel = EpisodeDetailsViewModel(
        getSingleEpisodeUseCase,
        getCharactersByIdUseCase,
        loadCharactersByIdUseCase
    )

    Given("Check provide episode details") {
        val id = 1
        val episode = EpisodeEntity(id, "", "", "", emptyList(), "", "")
        every { getSingleEpisodeUseCase.invoke(id) } returns flowOf(episode)

        When("viewModel.onViewCreated(episodeId: Int)") {
            viewModel.onViewCreated(id)

            Then("should provide given episode to flow") {
                verify { getSingleEpisodeUseCase.invoke(id) }
                viewModel.episodeState.test {
                    awaitItem() shouldBe episode
                }
            }
        }
    }

    Given("Check call loading characters") {
        val id = 1
        val episode = EpisodeEntity(id, "", "", "", emptyList(), "", "")
        every { getSingleEpisodeUseCase.invoke(id) } returns flowOf(episode)

        When("viewModel.onViewCreated(episodeId: Int)") {
            viewModel.onViewCreated(id)

            Then("should call loading characters") {
                coVerify { loadCharactersByIdUseCase.invoke(episode.charactersId) }
            }
        }
    }

    Given("Check provide empty characters list") {
        val id = 1
        val episode = EpisodeEntity(id, "", "", "", emptyList(), "", "")
        every { getSingleEpisodeUseCase.invoke(id) } returns flowOf(episode)
        every { getCharactersByIdUseCase.invoke(episode.charactersId) } returns flowOf(emptyList())

        When("viewModel.onViewCreated(episodeId: Int)") {
            viewModel.onViewCreated(id)

            Then("should provide empty characters list to flow") {
                viewModel.charactersListState.test {
                    awaitItem() shouldBe emptyList()
                }
            }
        }
    }

    Given("Check provide not empty characters list") {
        val episodeId = 1
        val characterId = 1
        val episode = EpisodeEntity(episodeId, "", "", "", listOf(characterId), "", "")
        val character = CharacterEntity(
            characterId,
            "",
            CharacterStatus.UNKNOWN,
            "",
            "",
            CharacterGender.UNKNOWN,
            -1,
            -1,
            "",
            emptyList(),
            "",
            ""
        )
        val characters = listOf(character)
        every { getSingleEpisodeUseCase.invoke(episodeId) } returns flowOf(episode)
        every { getCharactersByIdUseCase.invoke(episode.charactersId) } returns flowOf(characters)

        When("viewModel.onViewCreated(episodeId: Int)") {
            viewModel.onViewCreated(episodeId)

            Then("should get not empty list and provide it to flow") {
                verify { getCharactersByIdUseCase.invoke(episode.charactersId) }
                viewModel.charactersListState.test {
                    awaitItem() shouldBe characters
                }
            }
        }
    }

    Given("Check provide data on reload") {
        val id = 1
        val episode = EpisodeEntity(id, "", "", "", emptyList(), "", "")
        every { getSingleEpisodeUseCase.invoke(id) } returns flowOf(episode)
        every { getCharactersByIdUseCase.invoke(episode.charactersId) } returns flowOf(emptyList())

        When("viewModel.onButtonReloadPressed(episodeId: Int)") {
            viewModel.onButtonReloadPressed(id)

            Then("should provide episode and characters list to flow") {
                viewModel.episodeState.test {
                    awaitItem() shouldBe episode
                }
                viewModel.charactersListState.test {
                    awaitItem() shouldBe emptyList()
                }
            }
        }
    }

    Given("Check provide error if episode flow fails") {
        val id = 1
        every { getSingleEpisodeUseCase.invoke(id) } returns flow {
            throw RuntimeException()
        }

        When("viewModel.onViewCreated(episodeId: Int)") {
            viewModel.onViewCreated(id)

            Then("should provide error notification to flow") {
                viewModel.errorState.test {
                    awaitItem().shouldNotBeNull()
                }
            }
        }
    }

    Given("Check provide error if characters flow fails") {
        val id = 1
        val episode = EpisodeEntity(id, "", "", "", emptyList(), "", "")
        every { getSingleEpisodeUseCase.invoke(id) } returns flowOf(episode)
        every { getCharactersByIdUseCase.invoke(episode.charactersId) } returns flow {
            throw RuntimeException()
        }

        When("viewModel.onViewCreated(episodeId: Int)") {
            viewModel.onViewCreated(id)

            Then("should provide error notification to flow") {
                viewModel.errorState.test {
                    awaitItem().shouldNotBeNull()
                }
            }
        }
    }
})
