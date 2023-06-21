package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.common.showToast
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentLocationsBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.domain.entity.LocationEntity
import ru.ponomarchukpn.astonfinalproject.presentation.adapters.LocationsAdapter
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.LocationsViewModel

class LocationsFragment : BaseFragment<FragmentLocationsBinding, LocationsViewModel>(
    LocationsViewModel::class.java
) {

    private val adapter by lazy {
        LocationsAdapter(
            onListEnded = {
                viewModel.onListEnded()
                startProgress()
            },
            onItemClick = {
                launchDetailsFragment(it.id)
            }
        )
    }

    private var tabName: String? = null

    override fun createBinding(): FragmentLocationsBinding {
        return FragmentLocationsBinding.inflate(layoutInflater)
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
        configureSwipeLayout()
        setOnRefreshListener()
        setButtonFilterListener()
        setFilterChangedResultListener()
        setSearchViewListener()
        subscribeFlow()
        notifyViewModel()
        startProgress()
    }

    private fun parseArguments() {
        val args = requireArguments()
        tabName = args.getString(KEY_TAB_NAME)
    }

    private fun setAdapter() {
        binding.locationsRecycler.adapter = adapter
    }

    private fun configureSwipeLayout() {
        binding.locationsSwipeLayout.setProgressViewEndTarget(false, 0)
    }

    private fun setOnRefreshListener() {
        binding.locationsSwipeLayout.setOnRefreshListener {
            viewModel.onListSwiped()
            startProgress()
        }
    }

    private fun setButtonFilterListener() {
        binding.locationsButtonFilter.setOnClickListener {
            launchFilterFragment()
        }
    }

    private fun setFilterChangedResultListener() {
        setFragmentResultListener(KEY_FILTER_CHANGED) { key, bundle ->
            val isChanged = bundle.getBoolean(key)
            if (isChanged) {
                viewModel.onFilterSettingsChanged()
            }
        }
    }

    private fun setSearchViewListener() {
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText)
                return true
            }
        }
        binding.locationsSearch.setOnQueryTextListener(listener)
    }

    private fun subscribeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locationsListState
                    .onEach {
                        processLocationsList(it)
                    }
                    .launchIn(this)

                viewModel.notEmptyFilterState
                    .onEach {
                        setButtonClearState(it)
                    }
                    .launchIn(this)

                viewModel.errorState
                    .onEach {
                        showError()
                        stopProgress()
                    }
                    .launchIn(this)

                viewModel.emptyResultState
                    .onEach { showEmptyResultToast() }
                    .launchIn(this)
            }
        }
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated()
    }

    private fun processLocationsList(locations: List<LocationEntity>) {
        adapter.submitList(locations)
        stopProgress()
    }

    private fun startProgress() {
        binding.locationsProgress.visibility = View.VISIBLE
    }

    private fun stopProgress() {
        binding.locationsProgress.visibility = View.INVISIBLE
    }

    private fun showError() {
        val errorMessage = getString(R.string.error_downloading_list)
        requireContext().showToast(errorMessage)
    }

    private fun showEmptyResultToast() {
        val emptyResultMessage = getString(R.string.message_empty_results)
        requireContext().showToast(emptyResultMessage)
    }

    private fun setButtonClearState(notEmptyFilter: Boolean) {
        if (notEmptyFilter) {
            binding.locationsButtonClear.setBackgroundResource(R.drawable.colored_button_ripple)
            binding.locationsButtonClear.setOnClickListener {
                viewModel.onButtonClearPressed()
                startProgress()
            }
        } else {
            binding.locationsButtonClear.setBackgroundResource(R.drawable.gray_button_shape)
            binding.locationsButtonClear.setOnClickListener(null)
        }
    }

    private fun launchDetailsFragment(locationId: Int) {
        tabName?.let {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_container, LocationDetailsFragment.newInstance(locationId, it))
                .addToBackStack(it)
                .commit()
        }
    }

    private fun launchFilterFragment() {
        tabName?.let {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_container, LocationsFilterFragment.newInstance())
                .addToBackStack(it)
                .commit()
        }
    }

    companion object {

        const val KEY_FILTER_CHANGED = "locationsFilterChanged"
        private const val KEY_TAB_NAME = "tabName"

        fun newInstance(tabName: String) = LocationsFragment().apply {
            arguments = bundleOf(KEY_TAB_NAME to tabName)
        }
    }
}
