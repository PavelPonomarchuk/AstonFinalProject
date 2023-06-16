package ru.ponomarchukpn.astonfinalproject.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
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

    private var tabName: String? = null

    override fun createBinding(): FragmentEpisodesBinding {
        return FragmentEpisodesBinding.inflate(layoutInflater)
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
        setOnRefreshListener()
        setButtonClearListener()
        setButtonFilterListener()
        setFilterChangedResultListener()
        setSearchViewListener()
        subscribeFlow()
        notifyViewModel()
    }

    private fun parseArguments() {
        val args = requireArguments()
        tabName = args.getString(KEY_TAB_NAME)
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
        tabName?.let {
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_container, EpisodeDetailsFragment.newInstance(episodeId, it))
                .addToBackStack(it)
                .commit()
        }
    }

    private fun launchFilterFragment() {
        tabName?.let {
            requireActivity().supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_container, EpisodesFilterFragment.newInstance())
                .addToBackStack(it)
                .commit()
        }
    }

    companion object {

        const val KEY_FILTER_CHANGED = "episodesFilterChanged"
        private const val KEY_TAB_NAME = "tabName"

        fun newInstance(tabName: String) = EpisodesFragment().apply {
            arguments = bundleOf(KEY_TAB_NAME to tabName)
        }
    }
}
