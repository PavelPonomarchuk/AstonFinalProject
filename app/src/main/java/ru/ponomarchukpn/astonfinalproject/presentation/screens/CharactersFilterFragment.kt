package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentCharactersFilterBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterGender
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterStatus
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharactersFilterSettings
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.CharactersFilterViewModel

class CharactersFilterFragment :
    BaseFragment<FragmentCharactersFilterBinding, CharactersFilterViewModel>(
        CharactersFilterViewModel::class.java
    ) {

    private var restored: Boolean = false

    override fun createBinding(): FragmentCharactersFilterBinding {
        return FragmentCharactersFilterBinding.inflate(layoutInflater)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreState(savedInstanceState)
        setButtonBackListener()
        setButtonApplyListener()
        subscribeFilterSettingsFlow()
        subscribeFilterSavedFlow()
        notifyViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val statusPosition = binding.charactersFilterStatusSpinner.selectedItemPosition
        val genderPosition = binding.charactersFilterGenderSpinner.selectedItemPosition
        outState.putInt(KEY_STATUS_POSITION, statusPosition)
        outState.putInt(KEY_GENDER_POSITION, genderPosition)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            val statusPosition = it.getInt(KEY_STATUS_POSITION)
            val genderPosition = it.getInt(KEY_GENDER_POSITION)
            binding.charactersFilterStatusSpinner.setSelection(statusPosition)
            binding.charactersFilterGenderSpinner.setSelection(genderPosition)
            restored = true
        }
    }

    private fun setButtonBackListener() {
        binding.charactersFilterBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setButtonApplyListener() {
        binding.charactersFilterApply.setOnClickListener {
            val settings = currentFilterSettings()
            viewModel.onApplyPressed(settings)
        }
    }

    private fun currentFilterSettings(): CharactersFilterSettings {
        val name = binding.charactersFilterEditName.text.toString()
        val status =
            when (val statusPosition = binding.charactersFilterStatusSpinner.selectedItemPosition) {
                0 -> null
                1 -> CharacterStatus.ALIVE
                2 -> CharacterStatus.DEAD
                3 -> CharacterStatus.UNKNOWN
                else -> throw RuntimeException("Unknown spinner position: $statusPosition")
            }
        val species = binding.charactersFilterEditSpecies.text.toString()
        val type = binding.charactersFilterEditType.text.toString()
        val gender =
            when (val genderPosition = binding.charactersFilterGenderSpinner.selectedItemPosition) {
                0 -> null
                1 -> CharacterGender.FEMALE
                2 -> CharacterGender.MALE
                3 -> CharacterGender.GENDERLESS
                4 -> CharacterGender.UNKNOWN
                else -> throw RuntimeException("Unknown spinner position: $genderPosition")
            }
        return CharactersFilterSettings(name, status, species, type, gender)
    }

    private fun subscribeFilterSettingsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.charactersFilterState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    if (!restored) {
                        setFilterSettings(it)
                    }
                }
        }
    }

    private fun setFilterSettings(settings: CharactersFilterSettings) {
        binding.charactersFilterEditName.setText(settings.name)
        binding.charactersFilterStatusSpinner.setSelection(
            when (settings.status) {
                null -> 0
                CharacterStatus.ALIVE -> 1
                CharacterStatus.DEAD -> 2
                CharacterStatus.UNKNOWN -> 3
            }
        )
        binding.charactersFilterEditSpecies.setText(settings.species)
        binding.charactersFilterEditType.setText(settings.type)
        binding.charactersFilterGenderSpinner.setSelection(
            when (settings.gender) {
                null -> 0
                CharacterGender.FEMALE -> 1
                CharacterGender.MALE -> 2
                CharacterGender.GENDERLESS -> 3
                CharacterGender.UNKNOWN -> 4
            }
        )
    }

    private fun subscribeFilterSavedFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filterSavedState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    finishWithSettingsChanged()
                }
        }
    }

    private fun finishWithSettingsChanged() {
        setFragmentResult(
            CharactersFragment.KEY_FILTER_CHANGED,
            bundleOf(CharactersFragment.KEY_FILTER_CHANGED to true)
        )
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated()
    }

    companion object {

        private const val KEY_STATUS_POSITION = "statusPosition"
        private const val KEY_GENDER_POSITION = "genderPosition"

        fun newInstance() = CharactersFilterFragment()
    }
}
