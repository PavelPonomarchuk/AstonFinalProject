package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ponomarchukpn.astonfinalproject.R
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentEpisodesBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.presentation.adapters.EpisodesAdapter
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.EpisodesViewModel

class EpisodesFragment : BaseFragment<FragmentEpisodesBinding, EpisodesViewModel>(
    EpisodesViewModel::class.java
) {

    private val adapter by lazy {
        EpisodesAdapter(
            onListEnded = {
                viewModel.onListEnded()
            },
            onItemClick = {
                launchDetailsFragment(it.id)
            }
        )
    }

    override fun createBinding(): FragmentEpisodesBinding {
        return FragmentEpisodesBinding.inflate(layoutInflater)
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
        binding.episodesRecycler.adapter = adapter
    }

    private fun setOnRefreshListener() {
        binding.episodesSwipeLayout.setOnRefreshListener {
            viewModel.onListSwiped()
        }
    }

    private fun setButtonClearListener() {
        //TODO
    }

    private fun setButtonFilterListener() {
        binding.episodesButtonFilter.setOnClickListener {
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
        binding.episodesSearch.setOnQueryTextListener(listener)
    }

    private fun subscribeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.episodesListState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { episodes ->
                    adapter.submitList(episodes)
                }
        }
    }

    private fun notifyViewModel() {
        viewModel.onViewCreated()
    }

    private fun launchDetailsFragment(episodeId: Int) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.host_container, EpisodeDetailsFragment.newInstance(episodeId))
            .addToBackStack(null)
            .commit()
    }

    private fun launchFilterFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.host_container, EpisodesFilterFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    companion object {

        fun newInstance() = EpisodesFragment()
    }
}
