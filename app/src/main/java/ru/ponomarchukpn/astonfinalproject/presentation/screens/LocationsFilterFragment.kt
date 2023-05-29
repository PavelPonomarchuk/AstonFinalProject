package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
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

    private var restored: Boolean = false

    override fun createBinding(): FragmentLocationsFilterBinding {
        return FragmentLocationsFilterBinding.inflate(layoutInflater)
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

    private fun restoreState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            restored = true
        }
    }

    private fun setButtonBackListener() {
        binding.locationsFilterBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setButtonApplyListener() {
        binding.locationsFilterApply.setOnClickListener {
            val settings = currentFilterSettings()
            viewModel.onApplyPressed(settings)
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
                    if (!restored) {
                        setFilterSettings(it)
                    }
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
        setFragmentResult(
            LocationsFragment.KEY_FILTER_CHANGED,
            bundleOf(LocationsFragment.KEY_FILTER_CHANGED to true)
        )
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated()
    }

    companion object {

        fun newInstance() = LocationsFilterFragment()
    }
}
