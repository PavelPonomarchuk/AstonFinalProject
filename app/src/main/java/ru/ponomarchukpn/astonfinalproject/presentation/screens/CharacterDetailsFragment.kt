package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.R
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

    private var characterId = UNDEFINED_ID
    private var characterEntity: CharacterEntity? = null

    private val adapter by lazy {
        EpisodesAdapter(
            null,
            onItemClick = {
                launchEpisodeDetailsFragment(it.id)
            }
        )
    }

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
    }

    private fun parseArguments() {
        val args = requireArguments()
        characterId = args.getInt(KEY_CHARACTER_ID)
    }

    private fun setAdapter() {
        binding.characterDetailsRecyclerEpisodes.adapter = adapter
    }

    private fun setButtonBackListener() {
        binding.characterDetailsBack.setOnClickListener {
            //TODO
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
                    .onEach { setCharacterData(it) }
                    .launchIn(this)

                viewModel.originNameState
                    .onEach { setOriginName(it) }
                    .launchIn(this)

                viewModel.locationNameState
                    .onEach { setLocationName(it) }
                    .launchIn(this)

                viewModel.episodesListState
                    .onEach { setEpisodes(it) }
                    .launchIn(this)

                viewModel.errorState
                    .onEach { showError() }
                    .launchIn(this)
            }
        }
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
        binding.characterDetailsImage.load(character.imageUrl)
    }

    private fun setOriginName(name: String) {
        binding.characterDetailsTvOrigin.text = name
    }

    private fun setLocationName(name: String) {
        binding.characterDetailsTvLocation.text = name
    }

    private fun setEpisodes(episodes: List<EpisodeEntity>) {
        adapter.submitList(episodes)
    }

    private fun showError() {
        //TODO
        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated(characterId)
    }

    private fun launchEpisodeDetailsFragment(episodeId: Int) {
        //TODO
        childFragmentManager.beginTransaction()
            .replace(R.id.host_container, EpisodeDetailsFragment.newInstance(episodeId))
            .addToBackStack(null)
            .commit()

        //Toast.makeText(requireContext(), "id: $episodeId", Toast.LENGTH_SHORT).show()
    }

    //TODO вообще нужен ripple на origin и location для видимого эффекта клика
    private fun launchLocationDetailsFragment(locationId: Int) {
        //TODO
        Toast.makeText(requireContext(), "id: $locationId", Toast.LENGTH_SHORT).show()
    }

    companion object {

        private const val KEY_CHARACTER_ID = "characterId"
        private const val UNDEFINED_ID = 0

        fun newInstance(id: Int) = CharacterDetailsFragment().apply {
            arguments = bundleOf(KEY_CHARACTER_ID to id)
        }
    }
}
