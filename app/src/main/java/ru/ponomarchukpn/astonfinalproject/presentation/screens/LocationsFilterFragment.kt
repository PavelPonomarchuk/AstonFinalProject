package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentLocationsFilterBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationsFilterSettings
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.LocationsFilterViewModel

class LocationsFilterFragment :
    BaseFragment<FragmentLocationsFilterBinding, LocationsFilterViewModel>(
        LocationsFilterViewModel::class.java
    ) {

    private var settingsReceived: Boolean = false

    override fun createBinding(): FragmentLocationsFilterBinding {
        return FragmentLocationsFilterBinding.inflate(layoutInflater)
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
        binding.locationsFilterBack.setOnClickListener {
            //TODO закрывать фрагмент через result api
            //возвращать уведомление, что настройки не менялись
        }
    }

    private fun setButtonApplyListener() {
        binding.locationsFilterApply.setOnClickListener {
            if (settingsReceived) {
                val settings = currentFilterSettings()
                viewModel.onApplyPressed(settings)
            }
        }
    }

    private fun currentFilterSettings() = LocationsFilterSettings(
        name = binding.locationsFilterEditName.text.toString(),
        type = binding.locationsFilterEditType.text.toString(),
        dimension = binding.locationsFilterEditDimension.text.toString()
    )

    private fun subscribeFilterSettingsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.locationsFilterState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    setFilterSettings(it)
                    settingsReceived = true
                }
        }
    }

    private fun setFilterSettings(settings: LocationsFilterSettings) {
        binding.locationsFilterEditName.setText(settings.name)
        binding.locationsFilterEditType.setText(settings.type)
        binding.locationsFilterEditDimension.setText(settings.dimension)
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

        fun newInstance() = LocationsFilterFragment()
    }
}
