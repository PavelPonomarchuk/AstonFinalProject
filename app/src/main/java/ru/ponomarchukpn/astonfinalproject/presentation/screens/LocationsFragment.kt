package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentLocationsBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.presentation.adapters.LocationsAdapter
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.LocationsViewModel

class LocationsFragment : BaseFragment<FragmentLocationsBinding, LocationsViewModel>(
    LocationsViewModel::class.java
) {

    private val adapter by lazy {
        LocationsAdapter(
            onListEnded = {
                viewModel.onListEnded()
            },
            onItemClick = {
                launchDetailsFragment(it.id)
            }
        )
    }

    override fun createBinding(): FragmentLocationsBinding {
        return FragmentLocationsBinding.inflate(layoutInflater)
    }

    override fun injectDependencies(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setOnRefreshListener()
        setButtonClearListener()
        setButtonFilterListener()
        setSearchViewListener()
        subscribeFlow()
        notifyViewModel()
    }

    private fun setAdapter() {
        binding.locationsRecycler.adapter = adapter
    }

    private fun setOnRefreshListener() {
        binding.locationsSwipeLayout.setOnRefreshListener {
            viewModel.onListSwiped()
        }
    }

    private fun setButtonClearListener() {
        //TODO
    }

    private fun setButtonFilterListener() {
        binding.locationsButtonFilter.setOnClickListener {
            launchFilterFragment()
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
            viewModel.locationsListState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { locations ->
                    adapter.submitList(locations)
                }
        }
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated()
    }

    private fun launchDetailsFragment(locationId: Int) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.host_container, LocationDetailsFragment.newInstance(locationId))
            .addToBackStack(null)
            .commit()
    }

    private fun launchFilterFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.host_container, LocationsFilterFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    companion object {

        fun newInstance() = LocationsFragment()
    }
}
