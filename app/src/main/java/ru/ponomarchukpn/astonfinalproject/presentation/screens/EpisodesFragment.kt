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
import ru.ponomarchukpn.astonfinalproject.databinding.FragmentEpisodesBinding
import ru.ponomarchukpn.astonfinalproject.di.AppComponent
import ru.ponomarchukpn.astonfinalproject.domain.entity.EpisodeEntity
import ru.ponomarchukpn.astonfinalproject.presentation.adapters.EpisodesAdapter
import ru.ponomarchukpn.astonfinalproject.presentation.viewmodel.EpisodesViewModel

class EpisodesFragment : BaseFragment<FragmentEpisodesBinding, EpisodesViewModel>(
    EpisodesViewModel::class.java
) {

    private val adapter by lazy {
        EpisodesAdapter(
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
        binding.episodesRecycler.adapter = adapter
    }

    private fun configureSwipeLayout() {
        binding.episodesSwipeLayout.setProgressViewEndTarget(false, 0)
    }

    private fun setOnRefreshListener() {
        binding.episodesSwipeLayout.setOnRefreshListener {
            viewModel.onListSwiped()
            startProgress()
        }
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.episodesListState
                    .onEach {
                        processEpisodesList(it)
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

    private fun processEpisodesList(episodes: List<EpisodeEntity>) {
        adapter.submitList(episodes)
        stopProgress()
    }

    private fun startProgress() {
        binding.episodesProgress.visibility = View.VISIBLE
    }

    private fun stopProgress() {
        binding.episodesProgress.visibility = View.INVISIBLE
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
            binding.episodesButtonClear.setBackgroundResource(R.drawable.colored_button_ripple)
            binding.episodesButtonClear.setOnClickListener {
                viewModel.onButtonClearPressed()
                startProgress()
            }
        } else {
            binding.episodesButtonClear.setBackgroundResource(R.drawable.gray_button_shape)
            binding.episodesButtonClear.setOnClickListener(null)
        }
    }

    private fun launchDetailsFragment(episodeId: Int) {
        tabName?.let {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.main_container, EpisodeDetailsFragment.newInstance(episodeId, it))
                .addToBackStack(it)
                .commit()
        }
    }

    private fun launchFilterFragment() {
        tabName?.let {
            parentFragmentManager.beginTransaction()
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
