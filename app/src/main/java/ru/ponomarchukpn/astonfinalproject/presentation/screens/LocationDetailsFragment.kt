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
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentLocationDetailsBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.domain.entity.CharacterEntity
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import ru.ponomarchukpn.astonfinalproject.presentation.adapters.CharactersAdapter
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.LocationDetailsViewModel

class LocationDetailsFragment : BaseFragment<FragmentLocationDetailsBinding, LocationDetailsViewModel>(
    LocationDetailsViewModel::class.java
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
    private var locationId = UNDEFINED_ID
    private var locationEntity: LocationEntity? = null
    private var loadedCount = COUNT_START

    override fun createBinding(): FragmentLocationDetailsBinding {
        return FragmentLocationDetailsBinding.inflate(layoutInflater)
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
        locationId = args.getInt(KEY_LOCATION_ID)
        tabName = args.getString(KEY_TAB_NAME)
    }

    private fun setAdapter() {
        binding.locationDetailsRecyclerResidents.adapter = adapter
    }

    private fun setButtonBackListener() {
        binding.locationDetailsBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun subscribeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locationState
                    .onEach { processLocation(it) }
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

    private fun processLocation(location: LocationEntity) {
        setLocationData(location)
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

    private fun setLocationData(location: LocationEntity) {
        locationEntity = location
        binding.locationDetailsName.text = location.name
        binding.locationDetailsTypeText.text = location.type
        binding.locationDetailsDimensionText.text = location.dimension
    }

    private fun setCharacters(characters: List<CharacterEntity>) {
        adapter.submitList(characters)
    }

    private fun showErrorViews() {
        binding.locationDetailsTextError.visibility = View.VISIBLE
        binding.locationDetailsButtonReload.visibility = View.VISIBLE
        binding.locationDetailsButtonReload.setOnClickListener {
            viewModel.onButtonReloadPressed(locationId)
            hideErrorViews()
            showContentViews()
            startProgress()
        }
    }

    private fun hideErrorViews() {
        binding.locationDetailsTextError.visibility = View.INVISIBLE
        binding.locationDetailsButtonReload.visibility = View.INVISIBLE
        binding.locationDetailsButtonReload.setOnClickListener(null)
    }

    private fun showContentViews() {
        binding.locationDetailsName.visibility = View.VISIBLE
        binding.locationDetailsTypeLabel.visibility = View.VISIBLE
        binding.locationDetailsTypeText.visibility = View.VISIBLE
        binding.locationDetailsDimensionLabel.visibility = View.VISIBLE
        binding.locationDetailsDimensionText.visibility = View.VISIBLE
        binding.locationDetailsRecyclerResidents.visibility = View.VISIBLE
    }

    private fun hideContentViews() {
        binding.locationDetailsName.visibility = View.INVISIBLE
        binding.locationDetailsTypeLabel.visibility = View.INVISIBLE
        binding.locationDetailsTypeText.visibility = View.INVISIBLE
        binding.locationDetailsDimensionLabel.visibility = View.INVISIBLE
        binding.locationDetailsDimensionText.visibility = View.INVISIBLE
        binding.locationDetailsRecyclerResidents.visibility = View.GONE
    }

    private fun startProgress() {
        binding.locationDetailsProgress.visibility = View.VISIBLE
    }

    private fun stopProgress() {
        binding.locationDetailsProgress.visibility = View.INVISIBLE
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated(locationId)
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

        private const val KEY_LOCATION_ID = "locationId"
        private const val KEY_TAB_NAME = "tabName"
        private const val UNDEFINED_ID = 0
        private const val COUNT_START = 0
        private const val COUNT_EXPECTED = 2

        fun newInstance(id: Int, tabName: String) = LocationDetailsFragment().apply {
            arguments = bundleOf(KEY_LOCATION_ID to id, KEY_TAB_NAME to tabName)
        }
    }
}
