package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
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

    private var settingsReceived: Boolean = false

    override fun createBinding(): FragmentCharactersFilterBinding {
        return FragmentCharactersFilterBinding.inflate(layoutInflater)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    //TODO обработать смену конфигурации - сохранять новые значения в полях
    //и флаг
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButtonBackListener()
        setButtonApplyListener()
        subscribeFilterSettingsFlow()
        subscribeFilterSavedFlow()
        notifyViewModel()
    }

    private fun setButtonBackListener() {
        binding.charactersFilterBack.setOnClickListener {
            //TODO закрывать фрагмент через result api
            //возвращать уведомление, что настройки не менялись
        }
    }

    private fun setButtonApplyListener() {
        binding.charactersFilterApply.setOnClickListener {
            if (settingsReceived) {
                val settings = currentFilterSettings()
                viewModel.onApplyPressed(settings)
            }
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
                    setFilterSettings(it)
                    settingsReceived = true
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
        //TODO возвращать уведомление что настройки изменились и закрывать фрагмент
        //через result api
        Toast.makeText(requireContext(), "Settings changed", Toast.LENGTH_SHORT).show()
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated()
    }

    companion object {

        fun newInstance() = CharactersFilterFragment()
    }
}
