package ru.ponomarchukpn.astonfinalproject.presentation.viewmodel

import app.cash.turbine.test
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodesFilterSettings
import ru.ponomarchukpn.astonfinalproject.domain.usecases.GetEpisodesFilterUseCase
import ru.ponomarchukpn.astonfinalproject.domain.usecases.SaveEpisodesFilterUseCase

class EpisodesFilterViewModelTest : BehaviorSpec({

    val getEpisodesFilterUseCase = mockk<GetEpisodesFilterUseCase>()
    val saveEpisodesFilterUseCase = mockk<SaveEpisodesFilterUseCase>()
    val viewModel = EpisodesFilterViewModel(getEpisodesFilterUseCase, saveEpisodesFilterUseCase)

    Given("Check save filter settings") {
        val settings = EpisodesFilterSettings("", "")
        coEvery { saveEpisodesFilterUseCase.invoke(settings) } returns true

        When("viewModel.onApplyPressed()") {
            viewModel.onApplyPressed(settings)

            Then("should save settings and emit Unit to flow") {
                coVerify { saveEpisodesFilterUseCase.invoke(settings) }
                viewModel.filterSavedState.test {
                    awaitItem() shouldBe Unit
                }
            }
        }
    }

    Given("Check load filter settings") {
        val settings = EpisodesFilterSettings("", "")
        coEvery { getEpisodesFilterUseCase.invoke() } returns settings

        When("viewModel.onViewCreated()") {
            viewModel.onViewCreated()

            Then("should load settings and emit to flow") {
                coVerify { getEpisodesFilterUseCase.invoke() }
                viewModel.episodesFilterState.test {
                    awaitItem() shouldBe settings
                }
            }
        }
    }
})
