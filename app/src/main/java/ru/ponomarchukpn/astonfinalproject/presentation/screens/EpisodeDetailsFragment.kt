package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentEpisodeDetailsBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.presentation.adapters.CharactersAdapter
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.EpisodeDetailsViewModel

class EpisodeDetailsFragment : BaseFragment<FragmentEpisodeDetailsBinding, EpisodeDetailsViewModel>(
    EpisodeDetailsViewModel::class.java
) {

    private val adapter by lazy {
        CharactersAdapter(
            null,
            onItemClick = {
                launchCharacterDetailsFragment(it.id)
            }
        )
    }

    private var tabName: String? = null
    private var episodeId = UNDEFINED_ID
    private var episodeEntity: EpisodeEntity? = null
    private var loadedCount = COUNT_START

    override fun createBinding(): FragmentEpisodeDetailsBinding {
        return FragmentEpisodeDetailsBinding.inflate(layoutInflater)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setButtonBackListener()
        subscribeFlow()
        notifyViewModel()

        if (loadedCount == COUNT_START) {
            startProgress()
        }
    }

    private fun parseArguments() {
        val args = requireArguments()
        episodeId = args.getInt(KEY_EPISODE_ID)
        tabName = args.getString(KEY_TAB_NAME)
    }

    private fun setAdapter() {
        binding.episodeDetailsRecyclerCharacters.adapter = adapter
    }

    private fun setButtonBackListener() {
        binding.episodeDetailsBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun subscribeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.episodeState
                    .onEach { processEpisode(it) }
                    .launchIn(this)

                viewModel.charactersListState
                    .onEach { processCharacters(it) }
                    .launchIn(this)

                viewModel.errorState
                    .onEach { showError() }
                    .launchIn(this)
            }
        }
    }

    private fun processEpisode(episode: EpisodeEntity) {
        setEpisodeData(episode)
        checkLoadingCompleted()
    }

    private fun processCharacters(characters: List<CharacterEntity>) {
        setCharacters(characters)
        checkLoadingCompleted()
    }

    private fun checkLoadingCompleted() {
        loadedCount++
        if (loadedCount == COUNT_EXPECTED) {
            stopProgress()
        }
    }

    private fun showError() {
        hideContentViews()
        stopProgress()
        showErrorViews()
    }

    private fun setEpisodeData(episode: EpisodeEntity) {
        episodeEntity = episode
        binding.episodeDetailsName.text = episode.name
        binding.episodeDetailsAirDateText.text = episode.airDate
        binding.episodeDetailsCodeText.text = episode.episode
    }

    private fun setCharacters(characters: List<CharacterEntity>) {
        adapter.submitList(characters)
    }

    private fun showErrorViews() {
        binding.episodeDetailsTextError.visibility = View.VISIBLE
        binding.episodeDetailsButtonReload.visibility = View.VISIBLE
        binding.episodeDetailsButtonReload.setOnClickListener {
            viewModel.onButtonReloadPressed(episodeId)
            hideErrorViews()
            showContentViews()
            startProgress()
        }
    }

    private fun hideErrorViews() {
        binding.episodeDetailsTextError.visibility = View.INVISIBLE
        binding.episodeDetailsButtonReload.visibility = View.INVISIBLE
        binding.episodeDetailsButtonReload.setOnClickListener(null)
    }

    private fun showContentViews() {
        binding.episodeDetailsName.visibility = View.VISIBLE
        binding.episodeDetailsCodeLabel.visibility = View.VISIBLE
        binding.episodeDetailsCodeText.visibility = View.VISIBLE
        binding.episodeDetailsAirDateLabel.visibility = View.VISIBLE
        binding.episodeDetailsAirDateText.visibility = View.VISIBLE
        binding.episodeDetailsRecyclerCharacters.visibility = View.VISIBLE
    }

    private fun hideContentViews() {
        binding.episodeDetailsName.visibility = View.INVISIBLE
        binding.episodeDetailsCodeLabel.visibility = View.INVISIBLE
        binding.episodeDetailsCodeText.visibility = View.INVISIBLE
        binding.episodeDetailsAirDateLabel.visibility = View.INVISIBLE
        binding.episodeDetailsAirDateText.visibility = View.INVISIBLE
        binding.episodeDetailsRecyclerCharacters.visibility = View.GONE
    }

    private fun startProgress() {
        binding.episodeDetailsProgress.visibility = View.VISIBLE
    }

    private fun stopProgress() {
        binding.episodeDetailsProgress.visibility = View.INVISIBLE
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated(episodeId)
    }

    private fun launchCharacterDetailsFragment(characterId: Int) {
        tabName?.let {
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_container, CharacterDetailsFragment.newInstance(characterId, it))
                .addToBackStack(it)
                .commit()
        }
    }

    companion object {

        private const val KEY_EPISODE_ID = "episodeId"
        private const val KEY_TAB_NAME = "tabName"
        private const val UNDEFINED_ID = 0
        private const val COUNT_START = 0
        private const val COUNT_EXPECTED = 2

        fun newInstance(id: Int, tabName: String) = EpisodeDetailsFragment().apply {
            arguments = bundleOf(KEY_EPISODE_ID to id, KEY_TAB_NAME to tabName)
        }
    }
}
