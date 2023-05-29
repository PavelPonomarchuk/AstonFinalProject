package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.common.showToast
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentCharacterDetailsBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterGender
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterStatus
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.presentation.adapters.EpisodesAdapter
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.CharacterDetailsViewModel

class CharacterDetailsFragment :
    BaseFragment<FragmentCharacterDetailsBinding, CharacterDetailsViewModel>(
        CharacterDetailsViewModel::class.java
    ) {

    private val adapter by lazy {
        EpisodesAdapter(
            null,
            onItemClick = {
                launchEpisodeDetailsFragment(it.id)
            }
        )
    }

    private var tabName: String? = null
    private var characterId = UNDEFINED_ID
    private var characterEntity: CharacterEntity? = null
    private var loadedCount = COUNT_START

    override fun createBinding(): FragmentCharacterDetailsBinding {
        return FragmentCharacterDetailsBinding.inflate(layoutInflater)
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
        setOriginListener()
        setLocationListener()
        subscribeFlow()
        notifyViewModel()

        if (loadedCount == COUNT_START) {
            startProgress()
        }
    }

    private fun parseArguments() {
        val args = requireArguments()
        characterId = args.getInt(KEY_CHARACTER_ID)
        tabName = args.getString(KEY_TAB_NAME)
    }

    private fun setAdapter() {
        binding.characterDetailsRecyclerEpisodes.adapter = adapter
    }

    private fun setButtonBackListener() {
        binding.characterDetailsBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setOriginListener() {
        binding.characterDetailsOrigin.setOnClickListener {
            characterEntity?.let {
                launchLocationDetailsFragment(it.originId)
            }
        }
    }

    private fun setLocationListener() {
        binding.characterDetailsLocation.setOnClickListener {
            characterEntity?.let {
                launchLocationDetailsFragment(it.locationId)
            }
        }
    }

    private fun subscribeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.characterState
                    .onEach { processCharacter(it) }
                    .launchIn(this)

                viewModel.originNameState
                    .onEach { processOriginName(it) }
                    .launchIn(this)

                viewModel.locationNameState
                    .onEach { processLocationName(it) }
                    .launchIn(this)

                viewModel.episodesListState
                    .onEach { processEpisodes(it) }
                    .launchIn(this)

                viewModel.errorState
                    .onEach { showError() }
                    .launchIn(this)
            }
        }
    }

    private fun processCharacter(character: CharacterEntity) {
        setCharacterData(character)
        checkLoadingCompleted()
    }

    private fun processOriginName(name: String) {
        setOriginName(name)
        checkLoadingCompleted()
    }

    private fun processLocationName(name: String) {
        setLocationName(name)
        checkLoadingCompleted()
    }

    private fun processEpisodes(episodes: List<EpisodeEntity>) {
        setEpisodes(episodes)
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

    private fun setCharacterData(character: CharacterEntity) {
        characterEntity = character
        binding.characterDetailsName.text = character.name

        binding.characterDetailsStatusText.text = when (character.status) {
            CharacterStatus.ALIVE -> getString(R.string.status_alive)
            CharacterStatus.DEAD -> getString(R.string.status_dead)
            CharacterStatus.UNKNOWN -> getString(R.string.status_unknown)
        }
        binding.characterDetailsSpeciesText.text = character.species
        binding.characterDetailsTypeText.text = character.type

        binding.characterDetailsGenderText.text = when (character.gender) {
            CharacterGender.FEMALE -> getString(R.string.gender_female)
            CharacterGender.MALE -> getString(R.string.gender_male)
            CharacterGender.GENDERLESS -> getString(R.string.gender_genderless)
            CharacterGender.UNKNOWN -> getString(R.string.gender_unknown)
        }
        binding.characterDetailsImage.load(character.imageUrl) {
            error(R.drawable.placeholder_avatar)
        }
    }

    private fun setOriginName(name: String) {
        val labelOrigin = getString(R.string.app_label_origin)
        binding.characterDetailsTvOrigin.text = String.format("%s: %s", labelOrigin, name)
    }

    private fun setLocationName(name: String) {
        val labelLocation = getString(R.string.app_label_location)
        binding.characterDetailsTvLocation.text = String.format("%s: %s", labelLocation, name)
    }

    private fun setEpisodes(episodes: List<EpisodeEntity>) {
        adapter.submitList(episodes)
    }

    private fun showErrorViews() {
        binding.characterDetailsTextError.visibility = View.VISIBLE
        binding.characterDetailsButtonReload.visibility = View.VISIBLE
        binding.characterDetailsButtonReload.setOnClickListener {
            viewModel.onButtonReloadPressed(characterId)
            hideErrorViews()
            showContentViews()
            startProgress()
        }
    }

    private fun hideErrorViews() {
        binding.characterDetailsTextError.visibility = View.INVISIBLE
        binding.characterDetailsButtonReload.visibility = View.INVISIBLE
        binding.characterDetailsButtonReload.setOnClickListener(null)
    }

    private fun showContentViews() {
        binding.characterDetailsName.visibility = View.VISIBLE
        binding.characterDetailsScroll.visibility = View.VISIBLE
        binding.characterDetailsRecyclerEpisodes.visibility = View.VISIBLE
    }

    private fun hideContentViews() {
        binding.characterDetailsName.visibility = View.INVISIBLE
        binding.characterDetailsScroll.visibility = View.GONE
        binding.characterDetailsRecyclerEpisodes.visibility = View.GONE
    }

    private fun startProgress() {
        binding.characterDetailsProgress.visibility = View.VISIBLE
    }

    private fun stopProgress() {
        binding.characterDetailsProgress.visibility = View.INVISIBLE
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated(characterId)
    }

    private fun launchEpisodeDetailsFragment(episodeId: Int) {
        tabName?.let {
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_container, EpisodeDetailsFragment.newInstance(episodeId, it))
                .addToBackStack(it)
                .commit()
        }
    }

    private fun launchLocationDetailsFragment(locationId: Int) {
        if (locationId == UNDEFINED_LOCATION_ID) {
            showUnknownLocationToast()
            return
        }
        tabName?.let {
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_container, LocationDetailsFragment.newInstance(locationId, it))
                .addToBackStack(it)
                .commit()
        }
    }

    private fun showUnknownLocationToast() {
        val message = getString(R.string.message_unknown_location)
        requireContext().showToast(message)
    }

    companion object {

        private const val KEY_CHARACTER_ID = "characterId"
        private const val KEY_TAB_NAME = "tabName"
        private const val UNDEFINED_ID = 0
        private const val COUNT_START = 0
        private const val COUNT_EXPECTED = 4
        private const val UNDEFINED_LOCATION_ID = -1

        fun newInstance(id: Int, tabName: String) = CharacterDetailsFragment().apply {
            arguments = bundleOf(KEY_CHARACTER_ID to id, KEY_TAB_NAME to tabName)
        }
    }
}
