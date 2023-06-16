package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentEpisodesFilterBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodesFilterSettings
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.EpisodesFilterViewModel

class EpisodesFilterFragment :
    BaseFragment<FragmentEpisodesFilterBinding, EpisodesFilterViewModel>(
        EpisodesFilterViewModel::class.java
    ) {

    private var settingsReceived: Boolean = false

    override fun createBinding(): FragmentEpisodesFilterBinding {
        return FragmentEpisodesFilterBinding.inflate(layoutInflater)
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
        binding.episodesFilterBack.setOnClickListener {
            //TODO закрывать фрагмент через result api
            //возвращать уведомление, что настройки не менялись
        }
    }

    private fun setButtonApplyListener() {
        binding.episodesFilterApply.setOnClickListener {
            if (settingsReceived) {
                val settings = currentFilterSettings()
                viewModel.onApplyPressed(settings)
            }
        }
    }

    private fun currentFilterSettings() = EpisodesFilterSettings(
        name = binding.episodesFilterEditName.text.toString(),
        code = binding.episodesFilterEditCode.text.toString()
    )

    private fun subscribeFilterSettingsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.episodesFilterState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    setFilterSettings(it)
                    settingsReceived = true
                }
        }
    }

    private fun setFilterSettings(settings: EpisodesFilterSettings) {
        binding.episodesFilterEditName.setText(settings.name)
        binding.episodesFilterEditCode.setText(settings.code)
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

        fun newInstance() = EpisodesFilterFragment()
    }
}
